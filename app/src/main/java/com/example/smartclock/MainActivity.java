package com.example.smartclock;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.things.device.TimeManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    private static Context mContext;



    public void updateTime(){
        final SimpleDateFormat hrs = new SimpleDateFormat("hh");
        final SimpleDateFormat min = new SimpleDateFormat("mm");
        final SimpleDateFormat am = new SimpleDateFormat("aa");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
        final SimpleDateFormat sec = new SimpleDateFormat("ss");
        final SimpleDateFormat weekDayformt = new SimpleDateFormat("E");

        final TextView hrsView = findViewById(R.id.hours);
        final TextView minView = findViewById(R.id.minute);

        final TextView txt_ampm = findViewById(R.id.ampm);
        final TextView dateText = findViewById(R.id.date2);
        final TextView secText = findViewById(R.id.sec);
        final TextView weekDay = findViewById(R.id.weekday);
        //time.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        Date date = new Date();

        int seconds = Integer.valueOf(sec.format(date));
        hrsView.setText(hrs.format(date));
        minView.setText(min.format(date));
        txt_ampm.setText(am.format(date));
        dateText.setText(dateFormat.format(date));
        secText.setText(String.valueOf(seconds));

        weekDay.setText(weekDayformt.format(date));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTime();
            }
        }, 1000);
    }

    public void updateBlink(){
        final TextView tikView = findViewById(R.id.tiktik);

        if(tikView.getVisibility() == View.VISIBLE){
            tikView.setVisibility(View.INVISIBLE);
        } else {
            tikView.setVisibility(View.VISIBLE);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateBlink();
            }
        }, 1000);
    }

    public void updateWeather(){

        final ImageView icon = findViewById(R.id.weatherIcon);
        final TextView temp = findViewById(R.id.temp);
        final TextView humid = findViewById(R.id.humidity);
        final TextView wind = findViewById(R.id.wind);
        final TextView wtDesc = findViewById(R.id.wType);
        final LinearLayout layout = findViewById(R.id.activity_main);
        //final TextView pressureView = findViewById(R.id.pressure);
        final TextView visibilityView = findViewById(R.id.visibility);
        final ImageView hIcon = findViewById(R.id.hIcon),
                wIcon = findViewById(R.id.wIcon),
                vIcon = findViewById(R.id.vIcon);


        Weather weather = new Weather();
        JSONObject json = null;
        String response = null;
        String currentIcon = null;
        String currTemp = null;
        String currHumid = null;
        String currWindSped = null;
        String weatherType = null;
        String pressure = null;
        String visibility = null;

        try {
            response = weather.execute("").get();
            json = new JSONObject(response);

            JSONObject current = json.getJSONObject("current");
            JSONArray currentWeather = current.getJSONArray("weather");
            String weatherDescription = currentWeather.getJSONObject(0).getString("main");
            currentIcon = currentWeather.getJSONObject(0).getString("icon");
            currTemp = current.getString("temp");
            currHumid = current.getString("humidity");
            currWindSped = current.getString("wind_speed");
            //pressure = current.getString("pressure");
            visibility = current.getString("visibility");
            weatherType = currentWeather.getJSONObject(0).getString("main");
        }
        catch (Exception e) {
            Log.e("WeatherError","", e);
        }

        try{
            float bar = Float.valueOf(visibility)/1000;
            visibility = String.valueOf(bar) + " km";
        } catch(Exception e){
            visibility += " m";
        }


        final TextView hrsView = findViewById(R.id.hours);
        final TextView minView = findViewById(R.id.minute);
        final TextView tikView = findViewById(R.id.tiktik);

        final TextView txt_ampm = findViewById(R.id.ampm);
        final TextView dateText = findViewById(R.id.date2);
        final TextView secText = findViewById(R.id.sec);
        final TextView weekDay = findViewById(R.id.weekday);

        int dayColor = Color.parseColor("#AF6902");
        int nightColor = Color.parseColor("#CF3668");

        if(currentIcon.contains("n")){
            layout.setBackgroundResource(R.drawable.night);
            hIcon.setImageResource(R.drawable.humidity_n);
            wIcon.setImageResource(R.drawable.wind_n);
            vIcon.setImageResource(R.drawable.visibility_n);

            humid.setTextColor(nightColor);
            wind.setTextColor(nightColor);
            visibilityView.setTextColor(nightColor);
            temp.setTextColor(nightColor);
            wtDesc.setTextColor(nightColor);

            hrsView.setTextColor(nightColor);
            minView.setTextColor(nightColor);
            tikView.setTextColor(nightColor);
            txt_ampm.setTextColor(nightColor);
            secText.setTextColor(nightColor);
            weekDay.setTextColor(nightColor);
            dateText.setTextColor(nightColor);

        } else if(currentIcon.contains("d")){
            layout.setBackgroundResource(R.drawable.day);
            hIcon.setImageResource(R.drawable.humidity);
            wIcon.setImageResource(R.drawable.wind);
            vIcon.setImageResource(R.drawable.visibility);

            humid.setTextColor(dayColor);
            wind.setTextColor(dayColor);
            visibilityView.setTextColor(dayColor);
            temp.setTextColor(dayColor);
            wtDesc.setTextColor(dayColor);

            hrsView.setTextColor(dayColor);
            minView.setTextColor(dayColor);
            tikView.setTextColor(dayColor);
            txt_ampm.setTextColor(dayColor);
            secText.setTextColor(dayColor);
            weekDay.setTextColor(dayColor);
            dateText.setTextColor(dayColor);
        }
        float tempfloat =  Float.valueOf(currTemp);
        int tempVal = Math.round(tempfloat);
        temp.setText(", " + String.valueOf(tempVal) + "°c");
        humid.setText(currHumid + "%");
        wind.setText(currWindSped + " km/h");
        wtDesc.setText(weatherType);
        icon.setImageResource(weather.getIcon(currentIcon));
        //pressureView.setText(pressure + " hPa");
        visibilityView.setText(visibility);

        final Handler handler = new Handler();
        handler.postDelayed(
            new Runnable() {
                @Override
                public void run() {
                    updateWeather();
                }
            }, 300000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getActionBar().hide();
        mContext = getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTime();
        updateWeather();
        updateBlink();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}