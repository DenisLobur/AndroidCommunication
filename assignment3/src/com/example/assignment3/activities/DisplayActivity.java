package com.example.assignment3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.assignment3.R;
import com.example.assignment3.aidl.WeatherData;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by denis on 6/2/15.
 */
public class DisplayActivity extends LifecycleLoggingActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
        TextView name = (TextView)findViewById(R.id.name);
        TextView country = (TextView)findViewById(R.id.country);
        TextView sunrise = (TextView)findViewById(R.id.sunrise_val);
        TextView sunset = (TextView)findViewById(R.id.sunset_val);
        TextView main = (TextView)findViewById(R.id.main);
        TextView desc = (TextView)findViewById(R.id.description);
        TextView speed = (TextView)findViewById(R.id.speed_val);
        TextView deg = (TextView)findViewById(R.id.deg_val);
        TextView temp = (TextView)findViewById(R.id.temp_val);
        TextView humidity = (TextView)findViewById(R.id.humidity_val);
        TextView tempMin = (TextView)findViewById(R.id.tempmin_val);
        TextView tempMax = (TextView)findViewById(R.id.tempmax_val);
        TextView pressure = (TextView)findViewById(R.id.pressure_val);
        ImageView pic = (ImageView)findViewById(R.id.pic);

        ArrayList<WeatherData> list = intent.getParcelableArrayListExtra("par");
        WeatherData singleData = list.get(0);
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        Date date = new Date(singleData.getSunrise() * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String s = format.format(date);

        name.setText(singleData.getName());
        country.setText(singleData.getCountry());
        cal.setTimeInMillis(singleData.getSunrise()*1000);
        /*sunrise.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY))
                + ":" + String.valueOf(cal.get(Calendar.MINUTE)
                + ":" + String.valueOf(cal.get(Calendar.SECOND))));*/
        sunrise.setText(s);
        cal.setTimeInMillis(singleData.getSunset() * 1000);
        sunset.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY))
                + ":" + String.valueOf(cal.get(Calendar.MINUTE)
                + ":" + String.valueOf(cal.get(Calendar.SECOND))));
        main.setText(singleData.getMain());
        desc.setText(singleData.getDescription());
        speed.setText(String.valueOf(singleData.getSpeed()) + " m/h");
        deg.setText(String.valueOf(singleData.getDeg()) + " degree");
        temp.setText(String.valueOf(singleData.getTemp()) + " F");
        humidity.setText(String.valueOf(singleData.getHumidity()) +" %");
        tempMin.setText(String.valueOf(singleData.getTempMin()) + " F");
        tempMax.setText(String.valueOf(singleData.getTempMax()) + " F");
        pressure.setText(String.valueOf(singleData.getPressure()) + " mm");

        pic.setImageDrawable(null);

        int resId = 0;
        if(singleData.getIcon().equals("01d")) {
            resId = R.drawable.p01d;
        } else if (singleData.getIcon().equals("02d")) {
            resId = R.drawable.p02d;
        } else if (singleData.getIcon().equals("03d")) {
            resId = R.drawable.p03d;
        }else if (singleData.getIcon().equals("04d")) {
            resId = R.drawable.p04d;
        }else if (singleData.getIcon().equals("09d")) {
            resId = R.drawable.p09d;
        }else if (singleData.getIcon().equals("10d")) {
            resId = R.drawable.p10d;
        }else if (singleData.getIcon().equals("11d")) {
            resId = R.drawable.p11d;
        }else if (singleData.getIcon().equals("13d")) {
            resId = R.drawable.p13d;
        }else if (singleData.getIcon().equals("50d")) {
            resId = R.drawable.p50d;
        }else if (singleData.getIcon().equals("01n")) {
            resId = R.drawable.p01n;
        } else if (singleData.getIcon().equals("02n")) {
            resId = R.drawable.p02n;
        } else if (singleData.getIcon().equals("03n")) {
            resId = R.drawable.p03n;
        }else if (singleData.getIcon().equals("04n")) {
            resId = R.drawable.p04n;
        }else if (singleData.getIcon().equals("09n")) {
            resId = R.drawable.p09n;
        }else if (singleData.getIcon().equals("10n")) {
            resId = R.drawable.p10n;
        }else if (singleData.getIcon().equals("11n")) {
            resId = R.drawable.p11n;
        }else if (singleData.getIcon().equals("13n")) {
            resId = R.drawable.p13n;
        }else if (singleData.getIcon().equals("50n")) {
            resId = R.drawable.p50n;
        }
        pic.setBackgroundResource(resId);
    }
}
