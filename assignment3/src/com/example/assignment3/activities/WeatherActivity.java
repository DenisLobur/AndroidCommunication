package com.example.assignment3.activities;

import android.os.Bundle;
import com.example.assignment3.R;

public class WeatherActivity extends LifecycleLoggingActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
