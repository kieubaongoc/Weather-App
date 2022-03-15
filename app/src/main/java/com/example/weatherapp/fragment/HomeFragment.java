package com.example.weatherapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    private View mView;
    TextView txtCity, txtCountry, txtTemp, txtStatus, txtHumidity, txtCloud, txtWind, txtDay;
    ImageView imgIcon;

//    private IsendDataListener mIsendDataListener;

//    public interface IsendDataListener{
//        void sendData(String City);
//    }

    public HomeFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        mIsendDataListener = (IsendDataListener) getActivity();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_activity_home, container, false);
        AnhXa();
        String getCity = getArguments().getString("key");
        if (getCity.equals("")) {
            getCity = "Hanoi";
            GetCurrentWeatherData(getCity);
        } else {
            GetCurrentWeatherData(getCity);
        }
//        sendDataToFragment2();
        return mView;
    }

    private void AnhXa() {
        txtCity = (TextView) mView.findViewById(R.id.textviewCity);
        txtCountry = (TextView) mView.findViewById(R.id.textviewCountry);
        txtTemp = (TextView) mView.findViewById(R.id.textviewTemp);
        txtStatus = (TextView) mView.findViewById(R.id.textviewStatus);
        txtHumidity = (TextView) mView.findViewById(R.id.textviewHumi);
        txtCloud = (TextView) mView.findViewById(R.id.textviewCloud);
        txtWind = (TextView) mView.findViewById(R.id.textviewWind);
        txtDay = (TextView) mView.findViewById(R.id.textviewDay);
        imgIcon = mView.findViewById(R.id.imageIcon);
    }

    public void GetCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=65cfd157767c46c6f50248639d97778b";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("ketqua", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtCity.setText(name);

                            // Format days
                            long l = Long.valueOf(day);
                            Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy");
                            String Day = simpleDateFormat.format(date);
                            txtDay.setText(Day);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.with(getActivity().getApplicationContext()).load("http://openweathermap.org/img/wn/" + icon + ".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String temperature = jsonObjectMain.getString("temp");
                            String humidity = jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(temperature);
                            String temperatureFormat = String.valueOf(a.intValue());

                            txtTemp.setText(temperatureFormat + "ﾟC");
                            txtHumidity.setText(humidity + "%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            txtWind.setText(wind + "m/s");

                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String cloud = jsonObjectClouds.getString("all");
                            txtCloud.setText(cloud + "%");

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            txtCountry.setText(country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

//    private void sendDataToFragment2() {
//        String City = "Hanoi";
//        mIsendDataListener.sendData("hanoi");
//    }
}
