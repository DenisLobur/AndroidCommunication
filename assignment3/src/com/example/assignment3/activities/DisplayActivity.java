package com.example.assignment3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.assignment3.R;
import com.example.assignment3.aidl.WeatherData;

import java.util.ArrayList;

/**
 * Created by denis on 6/2/15.
 */
public class DisplayActivity extends LifecycleLoggingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        TextView t = (TextView)findViewById(R.id.ttt);
        Intent intent = getIntent();
        ArrayList<WeatherData> list = intent.getParcelableArrayListExtra("par");
        t.setText(list.get(0).toString());

    }
}
