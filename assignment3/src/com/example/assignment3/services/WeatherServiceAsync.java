package com.example.assignment3.services;

import android.content.Context;
import android.content.Intent;

/**
 * Created by denis on 6/1/15.
 */
public class WeatherServiceAsync extends LifecycleLoggingService {

    public static Intent makeIntent(Context context) {
        return new Intent(context,
                WeatherServiceAsync.class);
    }
}
