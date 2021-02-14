package com.example.smartclock;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather extends AsyncTask <String, Void, String>{

    private static final String apikey = "8b19f01582b28b72089e91a663bd984e";
    private static final String base = "https://api.openweathermap.org/data/2.5/onecall?lat=28.61282&lon=77.23114&exclude=minutely,hourly,alerts&appid=%s&units=metric";

    private HttpURLConnection con;
    private InputStream is;
    private String response;

    public void get_weatherData(){
        String url = String.format(base,apikey);

        try{
            URL _url = new URL(url);
            con = (HttpURLConnection) _url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            StringBuffer stringBuffer = new StringBuffer();
            is = con.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = br.readLine())!=null){
                stringBuffer.append(line + "rn");
            }

            is.close();
            con.disconnect();
            response = stringBuffer.toString();

        }
        catch (Exception e){
            String s = e.getMessage();
            Log.e("APIError",e.getMessage(),e);
        }

    }

    public String getResponse(){
        return  response;
    }

    public int getIcon(String code){
        switch (code){
            case "11d" :
            case "11n" :
                return  R.drawable.w11;
            case "09d" :
                return  R.drawable.w09d;
            case "09n" :
                return  R.drawable.w09n;
            case "10d" :
                return  R.drawable.w10d;
            case "10n" :
                return  R.drawable.w10n;
            case "01d" :
                return  R.drawable.w01d;
            case "01n" :
                return  R.drawable.w01n;
            case "02d" :
                return R.drawable.w02d;
            case "02n" :
                return R.drawable.w02n;
            case "03d" :
            case "03n" :
                return R.drawable.w03;
            case "04d" :
                return R.drawable.w04d;
            case "04n" :
                return R.drawable.w04n;
            case "13d" :
                return R.drawable.w13d;
            case "13n" :
                return R.drawable.w13n;
            case "50d" :
                return R.drawable.w50d;
            case "50n" :
                return R.drawable.w50n;


            default: return R.drawable.weather_none_available;

        }

    }

    @Override
    protected String doInBackground(String... strings) {
        get_weatherData();
        return response;
    }
}
