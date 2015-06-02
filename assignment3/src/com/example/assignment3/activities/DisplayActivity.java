package com.example.assignment3.activities;

import android.os.Bundle;
import com.example.assignment3.R;

/**
 * Created by denis on 6/2/15.
 */
public class DisplayActivity extends LifecycleLoggingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
    }
}
