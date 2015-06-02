package com.example.assignment3.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.example.assignment3.aidl.WeatherData;
import com.example.assignment3.aidl.WeatherRequest;
import com.example.assignment3.aidl.WeatherResults;
import com.example.assignment3.utils.Utils;

import java.util.List;

/**
 * Created by denis on 6/1/15.
 */
public class WeatherServiceAsync extends LifecycleLoggingService {

    public static Intent makeIntent(Context context) {
        return new Intent(context,
                WeatherServiceAsync.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherRequestImpl;
    }

    private final WeatherRequest.Stub mWeatherRequestImpl = new WeatherRequest.Stub(){

        @Override
        public void getCurrentWeather(String weather, WeatherResults results) throws RemoteException {
            final List<WeatherData> weatherResults = Utils.getResults(weather);

            if (weatherResults != null) {
                Log.d(TAG, ""
                        + weatherResults.size()
                        + " results for acronym: "
                        + weather);
                results.sendResults(weatherResults);
            } else {
                results.sendError("No weather for " + weather + " found");
            }
        }
    };
}
