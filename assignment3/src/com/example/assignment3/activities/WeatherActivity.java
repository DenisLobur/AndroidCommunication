package com.example.assignment3.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.assignment3.R;
import com.example.assignment3.operations.WeatherOps;
import com.example.assignment3.operations.WeatherOpsImpl;
import com.example.assignment3.utils.RetainedFragmentManager;

public class WeatherActivity extends LifecycleLoggingActivity implements View.OnClickListener {

    protected final RetainedFragmentManager mRetainedFragmentManager =
            new RetainedFragmentManager(this.getFragmentManager(),
                    TAG);
    
    private WeatherOps mWeatherOps;
    private Button loadSync;
    private Button loadAsync;
    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mWeatherOps = new WeatherOpsImpl(this);
        
        setContentView(R.layout.main);

        /*editText = (EditText) findViewById(R.id.enter_city);
        loadSync = (Button) findViewById(R.id.load_sync);
        loadAsync = (Button) findViewById(R.id.load_async);
        loadSync.setOnClickListener(this);
        loadAsync.setOnClickListener(this);*/
        
        handleConfigurationChanges();
    }

    @Override
    protected void onDestroy() {
        mWeatherOps.unbindService();
        
        super.onDestroy();
    }
    
    protected void handleConfigurationChanges(){
        if (mRetainedFragmentManager.firstTimeIn()) {
            Log.d(TAG, "First time onCreate() call");

            mWeatherOps = new WeatherOpsImpl(this);
            mRetainedFragmentManager.put("WEATHER_OPS_STATE", mWeatherOps);
            mWeatherOps.bindService();
        } else {
            Log.d(TAG, "Second or subsequent onCreate() call");

            mWeatherOps = mRetainedFragmentManager.get("WEATHER_OPS_STATE");

            if (mWeatherOps == null) {
                mWeatherOps = new WeatherOpsImpl(this);
                mRetainedFragmentManager.put("WEATHER_OPS_STATE", mWeatherOps);
                mWeatherOps.bindService();
            } else
                mWeatherOps.onConfigurationChange(this);
        }
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "click");
        switch (view.getId()){
            case R.id.load_sync:
                Log.d(TAG, "sync called");
                    mWeatherOps.expandWeatherSync(view);
                break;
            case R.id.load_async:
                Log.d(TAG, "async called");
                mWeatherOps.expandWeatherAsync(view);
                break;
        }
    }
}
