package com.example.assignment3.activities;

import android.os.Bundle;
import com.example.assignment3.R;
import com.example.assignment3.utils.RetainedFragmentManager;

public class WeatherActivity extends LifecycleLoggingActivity {

    protected final RetainedFragmentManager mRetainedFragmentManager =
            new RetainedFragmentManager(this.getFragmentManager(),
                    TAG);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
