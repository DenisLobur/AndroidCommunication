package com.example.assignment3.operations;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.assignment3.R;
import com.example.assignment3.activities.DisplayActivity;
import com.example.assignment3.activities.WeatherActivity;
import com.example.assignment3.aidl.WeatherCall;
import com.example.assignment3.aidl.WeatherData;
import com.example.assignment3.aidl.WeatherRequest;
import com.example.assignment3.aidl.WeatherResults;
import com.example.assignment3.services.WeatherServiceAsync;
import com.example.assignment3.services.WeatherServiceSync;
import com.example.assignment3.utils.GenericServiceConnection;
import com.example.assignment3.utils.Utils;
import com.example.assignment3.utils.WeatherDataArrayAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements all the Weather-related operations defined in
 * the WeatherOps interface.
 */
public class WeatherOpsImpl implements WeatherOps {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final String TAG = getClass().getSimpleName();

    /**
     * Used to enable garbage collection.
     */
    protected WeakReference<WeatherActivity> mActivity;
    	
    /**
     * The ListView that will display the results to the user.
     */
    protected WeakReference<ListView> mListView;

    /**
     * Weather entered by the user.
     */
    protected WeakReference<EditText> mEditText;

    protected WeakReference<Button> loadSync;
    protected WeakReference<Button> loadAsync;

    /**
     * List of results to display (if any).
     */
    protected List<WeatherData> mResults;

    /**
     * A custom ArrayAdapter used to display the list of WeatherData
     * objects.
     */
    protected WeakReference<WeatherDataArrayAdapter> mAdapter;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the WeatherServiceSync Service using bindService().
     */
    private GenericServiceConnection<WeatherCall> mServiceConnectionSync;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the WeatherServiceAsync Service using bindService().
     */
    private GenericServiceConnection<WeatherRequest> mServiceConnectionAsync;

    /**
     * This Handler is used to post Runnables to the UI from the
     * mWeatherResults callback methods to avoid a dependency on the
     * Activity, which may be destroyed in the UI Thread during a
     * runtime configuration change.
     */
    private final Handler mDisplayHandler = new Handler();

    /**
     * The implementation of the WeatherResults AIDL Interface, which
     * will be passed to the Weather Web service using the
     * WeatherRequest.expandWeather() method.
     * 
     * This implementation of WeatherResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private final WeatherResults.Stub mWeatherResults =
        new WeatherResults.Stub() {
            /**
             * This method is invoked by the WeatherServiceAsync to
             * return the results back to the WeatherActivity.
             */
            @Override
            public void sendResults(final List<WeatherData> WeatherDataList)
                throws RemoteException {
                // Since the Android Binder framework dispatches this
                // method in a background Thread we need to explicitly
                // post a runnable containing the results to the UI
                // Thread, where it's displayed.  We use the
                // mDisplayHandler to avoid a dependency on the
                // Activity, which may be destroyed in the UI Thread
                // during a runtime configuration change.
                mDisplayHandler.post(new Runnable() {
                        public void run() {
                            displayResults(WeatherDataList);
                        }
                    });
            }

            /**
             * This method is invoked by the WeatherServiceAsync to
             * return error results back to the WeatherActivity.
             */
            @Override
            public void sendError(final String reason)
                throws RemoteException {
                // Since the Android Binder framework dispatches this
                // method in a background Thread we need to explicitly
                // post a runnable containing the results to the UI
                // Thread, where it's displayed.  We use the
                // mDisplayHandler to avoid a dependency on the
                // Activity, which may be destroyed in the UI Thread
                // during a runtime configuration change.
                mDisplayHandler.post(new Runnable() {
                        public void run() {
                            Utils.showToast(mActivity.get(),
                                    reason);
                        }
                    });
            }
	};

    /**
     * Constructor initializes the fields.
     */
    public WeatherOpsImpl(WeatherActivity activity) {
        // Initialize the WeakReference.
        mActivity = new WeakReference<>(activity);

        // Finish the initialization steps.
        initializeViewFields();
        initializeNonViewFields();
    }

    /**
     * Initialize the View fields, which are all stored as
     * WeakReferences to enable garbage collection.
     */
    private void initializeViewFields() {
        // Get references to the UI components.
        mActivity.get().setContentView(R.layout.main);

        // Store the EditText that holds the urls entered by the user
        // (if any).
        mEditText = new WeakReference<>
            ((EditText) mActivity.get().findViewById(R.id.enter_city));

        loadSync = new WeakReference<>((Button) mActivity.get().findViewById(R.id.load_sync));
        loadAsync = new WeakReference<>((Button) mActivity.get().findViewById(R.id.load_async));
        loadSync.get().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandWeatherSync(v);
                //Intent intent = new Intent(mActivity.get(), DisplayActivity.class);
                //mActivity.get().startActivity(intent);
            }
        });

        loadAsync.get().setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                expandWeatherAsync(v);
            }
        });


        // Store the ListView for displaying the results entered.
        //TODO: finish later
        /*mListView = new WeakReference<>
            ((ListView) mActivity.get().findViewById(R.id.listView1));*/

        // Create a local instance of our custom Adapter for our
        // ListView.
        mAdapter = new WeakReference<>
            (new WeatherDataArrayAdapter(mActivity.get()));

        // Set the adapter to the ListView.
        /*mListView.get().setAdapter(mAdapter.get());*/

        // Display results if any (due to runtime configuration change).
        if (mResults != null)
            displayResults(mResults);
    }

    /**
     * (Re)initialize the non-view fields (e.g.,
     * GenericServiceConnection objects).
     */
    private void initializeNonViewFields() {
        mServiceConnectionSync = 
            new GenericServiceConnection<WeatherCall>
                (WeatherCall.class);

        mServiceConnectionAsync =
            new GenericServiceConnection<WeatherRequest>
                (WeatherRequest.class);
    }

    /**
     * Initiate the service binding protocol.
     */
    @Override
    public void bindService() {
        Log.d(TAG,
              "calling bindService()");

        // Launch the Weather Bound Services if they aren't already
        // running via a call to bindService(), which binds this
        // activity to the WeatherService* if they aren't already
        // bound.
        if (mServiceConnectionSync.getInterface() == null) 
            mActivity.get().getApplicationContext().bindService
                (WeatherServiceSync.makeIntent(mActivity.get()),
                 mServiceConnectionSync,
                 Context.BIND_AUTO_CREATE);

        if (mServiceConnectionAsync.getInterface() == null) 
            mActivity.get().getApplicationContext().bindService
                (WeatherServiceAsync.makeIntent(mActivity.get()),
                 mServiceConnectionAsync,
                 Context.BIND_AUTO_CREATE);
    }

    /**
     * Initiate the service unbinding protocol.
     */
    @Override
    public void unbindService() {
        if (mActivity.get().isChangingConfigurations()) 
            Log.d(TAG,
                  "just a configuration change - unbindService() not called");
        else {
            Log.d(TAG,
                  "calling unbindService()");

            // Unbind the Async Service if it is connected.
            if (mServiceConnectionAsync.getInterface() != null)
                mActivity.get().getApplicationContext().unbindService
                    (mServiceConnectionAsync);

            // Unbind the Sync Service if it is connected.
            if (mServiceConnectionSync.getInterface() != null)
                mActivity.get().getApplicationContext().unbindService
                    (mServiceConnectionSync);
        }
    }

    /**
     * Called after a runtime configuration change occurs to finish
     * the initialization steps.
     */
    public void onConfigurationChange(WeatherActivity activity) {
        Log.d(TAG,
              "onConfigurationChange() called");

        // Reset the mActivity WeakReference.
        mActivity = new WeakReference<>(activity);

        // (Re)initialize all the View fields.
        initializeViewFields();
    }

    /*
     * Initiate the asynchronous Weather lookup when the user presses
     * the "Look Up Async" button.
     */
    public void expandWeatherAsync(View v) {
        final WeatherRequest WeatherRequest = 
            mServiceConnectionAsync.getInterface();

        if (WeatherRequest != null) {
            // Get the Weather entered by the user.
            final String weather =
                mEditText.get().getText().toString();

            resetDisplay();

            try {
                // Invoke a one-way AIDL call, which does not block
                // the client.  The results are returned via the
                // sendResults() method of the mWeatherResults
                // callback object, which runs in a Thread from the
                // Thread pool managed by the Binder framework.
                WeatherRequest.getCurrentWeather(weather,
                        mWeatherResults);
            } catch (RemoteException e) {
                Log.e(TAG,
                      "RemoteException:" 
                      + e.getMessage());
            }
        } else {
            Log.d(TAG,
                  "WeatherRequest was null.");
        }
    }

    /*
     * Initiate the synchronous Weather lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void expandWeatherSync(View v) {
        final WeatherCall WeatherCall = 
            mServiceConnectionSync.getInterface();

        if (WeatherCall != null) {
            // Get the Weather entered by the user.
            final String weather =
                checkEmptyCity(mEditText.get().getText().toString());


            resetDisplay();

            // Use an anonymous AsyncTask to download the Weather data
            // in a separate thread and then display any results in
            // the UI thread.
            new AsyncTask<String, Void, List<WeatherData>> () {
                /**
                 * Weather we're trying to expand.
                 */
                private String mWeather;

                /**
                 * Retrieve the expanded Weather results via a
                 * synchronous two-way method call, which runs in a
                 * background thread to avoid blocking the UI thread.
                 */
                protected List<WeatherData> doInBackground(String... weathers) {
                    try {
                        mWeather = weathers[0];
                        return WeatherCall.getCurrentWeather(mWeather);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                /**
                 * Display the results in the UI Thread.
                 */
                protected void onPostExecute(List<WeatherData> WeatherDataList) {
                    if (WeatherDataList.size() > 0)
                        displayResults(WeatherDataList);
                    else 
                        Utils.showToast(mActivity.get(),
                                        "no expansions for "
                                        + mWeather
                                        + " found");
                }
                // Execute the AsyncTask to expand the Weather without
                // blocking the caller.
            }.execute(weather);
        } else {
            Log.d(TAG, "mWeatherCall was null.");
        }
    }

    private String checkEmptyCity (String city){
        if (!TextUtils.isEmpty(city)) {
            return city;
        } else {
            Toast.makeText(mActivity.get(), "Enter a city", Toast.LENGTH_SHORT).show();
            return "aaaaaaaaa";
        }
    }

    /**
     * Display the results to the screen.
     * 
     * @param results
     *            List of Results to be displayed.
     */
    private void displayResults(List<WeatherData> results) {
        mResults = results;

        // Set/change data set.
        //mAdapter.get().clear();
        //mAdapter.get().addAll(mResults);
        //mAdapter.get().notifyDataSetChanged();
        Intent intent = new Intent(mActivity.get(), DisplayActivity.class);
        intent.putParcelableArrayListExtra("par", (ArrayList<WeatherData>)mResults);
        mActivity.get().startActivity(intent);
    }

    /**
     * Reset the display prior to attempting to expand a new Weather.
     */
    private void resetDisplay() {
        Utils.hideKeyboard(mActivity.get(),
                           mEditText.get().getWindowToken());
        mResults = null;
        //mAdapter.get().clear();
        //mAdapter.get().notifyDataSetChanged();
    }
}
