package com.example.prgoh.dailymaker.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prgoh.dailymaker.Item.Weather;
import com.example.prgoh.dailymaker.R;
import com.example.prgoh.dailymaker.Util.ClockThread;
import com.example.prgoh.dailymaker.Util.Fonts;
import com.example.prgoh.dailymaker.databinding.FragmentLogoBinding;
import com.example.prgoh.dailymaker.databinding.FragmentWeatherBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by prgoh on 2017-09-06.
 */

public class FragmentWeather extends Fragment {

    private final int SKY = 500;
    private final int FOREGROUND = 501;

    private LinearLayout linearLayout;

    private Animation fadeIn, fadeOut, translateUp, translateDown, rotate;

    private boolean isClicked = false;

    private ClockThread clockThread;

    private Context context;
    private FragmentWeatherBinding binding;

    public FragmentWeather() {
//        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        context = container.getContext();
        Fonts.setGlobalFont(context, view);

        linearLayout = view.findViewById(R.id.linear);

        binding = DataBindingUtil.bind(view);

        binding.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandView(isClicked);
            }
        });
        binding.gpsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotate = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
                binding.gpsUpdate.setAnimation(rotate);

                getLocation();
            }
        });


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
        binding.dateTv.setText(format.format(calendar.getTime()));


        clockThread = new ClockThread(binding.hour, binding.am, binding.minute, binding.sec, binding.beep);
        clockThread.start();

        getLocation();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clockThread.interrupt();
    }

    private void getWeatherData(double lat, double lng) {
        String url = "http://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lng +
                "&units=metric&cnt=10&appid=cb29a626eaf5e41c4be058721ac45917";
        new ReceiveWeatherTask().execute(url);
        Log.e("lat, lon", "" + lat + "\t" + lng);
    }

    private class ReceiveWeatherTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while ((readed = in.readLine()) != null) {
                        JSONObject jsonObject = new JSONObject(readed);
                        return jsonObject;
                    }
                } else {
                    return null;
                }
            } catch (MalformedURLException e) {

            } catch (IOException e) {

            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if (jsonObject != null) {

                String nowTemp = "";
                String humidity = "";
                String main = "";
                String description = "";

                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
//                    Log.e("jsonarray", "" + jsonArray.toString());

                    ArrayList<Weather> data = new ArrayList<>();

//                    Log.e("JSONArray Length", "" + jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Weather weather = new Weather();

                        JSONObject object = jsonArray.getJSONObject(i);
//                        Log.e("object", "" + object.toString());
                        JSONObject obj = object.getJSONObject("main");
//                        Log.e("obj", "" + obj.toString());

                        description = object.getJSONArray("weather").getJSONObject(0).getString("description");
                        main = object.getJSONArray("weather").getJSONObject(0).getString("main");

                        weather.setSky(transferWeather(main, description));
//                        Log.e("sky", "" + weather.getSky());

                        humidity = obj.getString("humidity");
                        weather.setPop(humidity);
//                        Log.e("pop", "강수 : " + weather.getPop());

                        nowTemp = obj.getString("temp");
                        weather.setTemp(obj.getString("temp"));
//                        Log.e("temp", "" + weather.getTemp());

                        String hour = object.getString("dt_txt");
//                        Log.e("hour", "" + hour);
                        weather.setHour(hour);

                        data.add(weather);
                    }

                    // 여기부터 for 문으로
//                    JSONObject object = jsonArray.getJSONObject(0);
//                    Log.e("object", ""+object.toString());
//
//                    JSONObject obj = object.getJSONObject("main");
//                    Log.e("obj", ""+obj.toString());
//
//                    nowTemp = obj.getString("temp");
//                    humidity = obj.getString("humidity");
//
//                    main = object.getJSONArray("weather").getJSONObject(0).getString("main");
//                    description = object.getJSONArray("weather").getJSONObject(0).getString("description");

                    setView(data);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Receive error", "error");
                }

//                description = transferWeather(description);

//                final String msg = description + "습도" + humidity + "%, 현재 온도 : " + nowTemp;
//                Log.e("msg", "" + msg + "\t main : " + main); // TODO: 2017-09-04 main데이터 출력 형태를 확인 후, description 와 조합 후 transferweather 완성시키기

            }
        }
    }

    private void setImg(ImageView img, String sky, int type) {
        switch (type) {
            case SKY: {
                if (Integer.parseInt(sky) != 0) {
                    img.setImageResource(R.drawable.time_rain);
                } else {
                    int skyCode = Integer.parseInt(sky);
                    if (skyCode == 1)
                        img.setImageResource(R.drawable.time_sunny);
                    else if (skyCode == 2)
                        img.setImageResource(R.drawable.time_cloud);
                    else if (skyCode == 3)
                        img.setImageResource(R.drawable.time_cloud_high);
                    else if (skyCode == 4)
                        img.setImageResource(R.drawable.time_sun);
                }
                break;
            }
            case FOREGROUND: {
                if (Integer.parseInt(sky) != 0) {
                    img.setImageResource(R.drawable.rain);
                } else {
                    int skyCode = Integer.parseInt(sky);
                    if (skyCode == 1)
                        img.setImageResource(R.drawable.sunny);
                    else if (skyCode == 2)
                        img.setImageResource(R.drawable.cloud_low);
                    else if (skyCode == 3)
                        img.setImageResource(R.drawable.cloud_high);
                    else if (skyCode == 4)
                        img.setImageResource(R.drawable.fore_sun);
                }
                break;
            }
        }
    }

    private void setView(ArrayList<Weather> data) {

        Weather weather = data.get(0);

        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        setImg(binding.forgroundSky, weather.getSky(), FOREGROUND);
        binding.forgroundSky.setAnimation(fadeIn);
        binding.forgroundState.setText(transferWeather(weather.getSky()));
        binding.forgroundState.setAnimation(fadeIn);
        binding.forgroundRain.setText(weather.getPop() + " %");
        binding.forgroundRain.setAnimation(fadeIn);
        binding.popLayout.setVisibility(View.VISIBLE);
        binding.popLayout.setAnimation(fadeIn);
        binding.forgroundTemp.setText("섭씨 " + weather.getTemp());
        binding.forgroundTemp.setAnimation(fadeIn);

        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        binding.timePerTemp1.setText(weather.getTemp());
        binding.timePerTemp1.setAnimation(fadeIn);
        setImg(binding.timePerSky1, weather.getSky(), SKY);
        binding.timePerSky1.setAnimation(fadeIn);
        setTime(binding.timePerTime1, weather.getHour());
        binding.timePerTime1.setAnimation(fadeIn);
        binding.timePerRain1.setText(weather.getPop() + "%");
        binding.timePerRain1.setAnimation(fadeIn);

        weather = data.get(1);

        binding.timePerTemp2.setText(weather.getTemp());
        binding.timePerTemp2.setAnimation(fadeIn);
        setImg(binding.timePerSky2, weather.getSky(), SKY);
        binding.timePerSky2.setAnimation(fadeIn);
        setTime(binding.timePerTime2, weather.getHour());
        binding.timePerTime2.setAnimation(fadeIn);
        binding.timePerRain2.setText(weather.getPop() + "%");
        binding.timePerRain2.setAnimation(fadeIn);

        weather = data.get(2);

        binding.timePerTemp3.setText(weather.getTemp());
        binding.timePerTemp3.setAnimation(fadeIn);
        setImg(binding.timePerSky3, weather.getSky(), SKY);
        binding.timePerSky3.setAnimation(fadeIn);
        setTime(binding.timePerTime3, weather.getHour());
        binding.timePerTime3.setAnimation(fadeIn);
        binding.timePerRain3.setText(weather.getPop() + "%");
        binding.timePerRain3.setAnimation(fadeIn);

        weather = data.get(3);

        binding.timePerTemp4.setText(weather.getTemp());
        binding.timePerTemp4.setAnimation(fadeIn);
        setImg(binding.timePerSky4, weather.getSky(), SKY);
        binding.timePerSky4.setAnimation(fadeIn);
        setTime(binding.timePerTime4, weather.getHour());
        binding.timePerTime4.setAnimation(fadeIn);
        binding.timePerRain4.setText(weather.getPop() + "%");
        binding.timePerRain4.setAnimation(fadeIn);

        weather = data.get(4);

        binding.timePerTemp5.setText(weather.getTemp());
        binding.timePerTemp5.setAnimation(fadeIn);
        setImg(binding.timePerSky5, weather.getSky(), SKY);
        binding.timePerSky5.setAnimation(fadeIn);
        setTime(binding.timePerTime5, weather.getHour());
        binding.timePerTime5.setAnimation(fadeIn);
        binding.timePerRain5.setText(weather.getPop() + "%");
        binding.timePerRain5.setAnimation(fadeIn);

        weather = data.get(5);

        binding.timePerTemp6.setText(weather.getTemp());
        binding.timePerTemp6.setAnimation(fadeIn);
        setImg(binding.timePerSky6, weather.getSky(), SKY);
        binding.timePerSky6.setAnimation(fadeIn);
        setTime(binding.timePerTime6, weather.getHour());
        binding.timePerTime6.setAnimation(fadeIn);
        binding.timePerRain6.setText(weather.getPop() + "%");
        binding.timePerRain6.setAnimation(fadeIn);

        weather = data.get(6);

        binding.timePerTemp7.setText(weather.getTemp());
        binding.timePerTemp7.setAnimation(fadeIn);
        setImg(binding.timePerSky7, weather.getSky(), SKY);
        binding.timePerSky7.setAnimation(fadeIn);
        setTime(binding.timePerTime7, weather.getHour());
        binding.timePerTime7.setAnimation(fadeIn);
        binding.timePerRain7.setText(weather.getPop() + "%");
        binding.timePerRain7.setAnimation(fadeIn);

        weather = data.get(7);

        binding.timePerTemp8.setText(weather.getTemp());
        binding.timePerTemp8.setAnimation(fadeIn);
        setImg(binding.timePerSky8, weather.getSky(), SKY);
        binding.timePerSky8.setAnimation(fadeIn);
        setTime(binding.timePerTime8, weather.getHour());
        binding.timePerTime8.setAnimation(fadeIn);
        binding.timePerRain8.setText(weather.getPop() + "%");
        binding.timePerRain8.setAnimation(fadeIn);

        weather = data.get(8);

        binding.timePerTemp9.setText(weather.getTemp());
        binding.timePerTemp9.setAnimation(fadeIn);
        setImg(binding.timePerSky9, weather.getSky(), SKY);
        binding.timePerSky9.setAnimation(fadeIn);
        setTime(binding.timePerTime9, weather.getHour());
        binding.timePerTime9.setAnimation(fadeIn);
        binding.timePerRain9.setText(weather.getPop() + "%");
        binding.timePerRain9.setAnimation(fadeIn);

        weather = data.get(9);

        binding.timePerTemp10.setText(weather.getTemp());
        binding.timePerTemp10.setAnimation(fadeIn);
        setImg(binding.timePerSky10, weather.getSky(), SKY);
        binding.timePerSky10.setAnimation(fadeIn);
        setTime(binding.timePerTime10, weather.getHour());
        binding.timePerTime10.setAnimation(fadeIn);
        binding.timePerRain10.setText(weather.getPop() + "%");
        binding.timePerRain10.setAnimation(fadeIn);
    }

    private void setTime(TextView tv, String hour) {
        String[] timeS = hour.split(" +");
        String t = timeS[1].substring(0, 2);

        int time = Integer.parseInt(t);
        String tm = "";

        if (time < 12) {
            if (time == 0)
                tm += "오전 " + (time + 12) + "시";
            else
                tm += "오전 " + time + "시";
        } else {
            if (time == 12)
                tm += "오후 " + (time) + "시";
            else if (time == 24)
                tm += "오전 " + (time - 12) + "시";
            else
                tm += "오후 " + (time - 12) + "시";
        }

        tv.setText(tm);

    }

    private void getLocation() {
        //gps 위치정보 받아오기
        LocationManager location = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);

        String provider = location.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location loc = location.getLastKnownLocation(provider);

        double latitude = loc.getLatitude();
        double longtidue = loc.getLongitude();

        Geocoder geocoder = new Geocoder(context, Locale.KOREA);

        try{
            List<Address> addresses = geocoder.getFromLocation(latitude, longtidue, 1);
            Address address = addresses.get(0);

            binding.forgroundLocal.setText(address.getLocality() + " " + address.getThoroughfare());

        } catch (IOException e) {
            e.printStackTrace();
        }

        getWeatherData(latitude, longtidue);
    }



    private void expandView(boolean isClicked){
        if (!isClicked) {
            fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            translateUp = AnimationUtils.loadAnimation(context, R.anim.translate_up);

            binding.timeLayout.setVisibility(View.GONE);
            binding.popLayout.setVisibility(View.GONE);
            binding.popLayout.setAnimation(fadeOut);
            binding.forgroundSky.setVisibility(View.GONE);
            binding.forgroundSky.setAnimation(fadeOut);
            binding.forgroundTemp.setVisibility(View.GONE);
            binding.forgroundTemp.setAnimation(fadeOut);

            binding.expand.setAnimation(translateUp);
            binding.timePerLayout.setAnimation(translateUp);
            binding.timePerLayout2.setAnimation(translateUp);

            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            binding.expand.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            binding.expand.setText("시간별 ▽");
            this.isClicked = true;
        }else{
            fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            translateDown = AnimationUtils.loadAnimation(context, R.anim.translate_down);

            binding.timeLayout.setVisibility(View.VISIBLE);
            binding.timeLayout.setAnimation(fadeIn);
            binding.popLayout.setVisibility(View.VISIBLE);
            binding.popLayout.setAnimation(fadeIn);
            binding.forgroundSky.setVisibility(View.VISIBLE);
            binding.forgroundSky.setAnimation(fadeIn);
            binding.forgroundTemp.setVisibility(View.VISIBLE);
            binding.forgroundTemp.setAnimation(fadeIn);

            binding.timePerLayout.setAnimation(translateDown);

            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            binding.expand.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            binding.expand.setText("시간별 △");
            this.isClicked = false;
        }
    }

    private String transferWeather(String weather){
        int skyCode = Integer.parseInt(weather);

        switch (skyCode){
            case 1 : {
                weather = "맑음";
                break;
            }
            case 2 : {
                weather = "구름 조금";
                break;
            }
            case 3 :{
                weather = "구름 많음";
                break;
            }
            case 4 : {
                weather = "흐림";
                break;
            }
            default: {
                weather = "비";
                break;
            }
        }

        return weather;
    }

    private String transferWeather(String main, String weather){
        weather = weather.toLowerCase();
        main = main.toLowerCase();

        switch (weather){
            case "snow": {
                weather = "-1";
                break;
            }
            case "rain" : {
                weather = "-1";
                break;
            }
            case "drizzle" : {
                weather = "-1";
                break;
            }
            case "clear" : {
                weather = "1";
                break;
            }
            case "clouds" :{
                if (main.equals("few clouds") & main.equals("scattered clouds")){
                    weather = "2";
                }else{
                    weather = "3";
                }
                break;
            }
            default: {
                weather = "-1";
                break;
            }
        }

        return weather;
    }
}
