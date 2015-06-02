package com.example.assignment3.operations;

import android.view.View;
import com.example.assignment3.activities.WeatherActivity;

/**
 * This class defines all the Weather-related operations.
 */
public interface WeatherOps {
    /**
     * Initiate the service binding protocol.
     */
    void bindService();

    /**
     * Initiate the service unbinding protocol.
     */
    void unbindService();

    /*
     * Initiate the synchronous Weather lookup when the user presses
     * the "Look Up Sync" button.
     */
    void expandWeatherSync(View v);

    /*
     * Initiate the asynchronous Weather lookup when the user presses
     * the "Look Up Async" button.
     */
    void expandWeatherAsync(View v);

    /**
     * Called after a runtime configuration change occurs to finish
     * the initialization steps.
     */
    void onConfigurationChange(WeatherActivity activity);
}
