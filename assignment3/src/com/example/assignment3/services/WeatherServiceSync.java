package com.example.assignment3.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.example.assignment3.aidl.WeatherCall;
import com.example.assignment3.aidl.WeatherData;
import com.example.assignment3.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 6/1/15.
 */
public class WeatherServiceSync extends LifecycleLoggingService {

    public static Intent makeIntent(Context context) {
        return new Intent(context,
                WeatherServiceSync.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherCallImpl;
    }

    private final WeatherCall.Stub mWeatherCallImpl = new WeatherCall.Stub() {

        @Override
        public List<WeatherData> getCurrentWeather(String weather) throws RemoteException {
            final List<WeatherData> weatherResults = Utils.getResults(weather);

            if(weatherResults != null) {
                Log.d(TAG, "" + weatherResults.size() + " results for weather " + weather);
                return  weatherResults;
            } else {
                return new ArrayList<WeatherData>();
            }
        }
    };
}
