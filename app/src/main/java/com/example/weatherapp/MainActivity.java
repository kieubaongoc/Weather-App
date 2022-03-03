package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText editSearch;
    Button btnSearch, btnChangeActivity;
    ImageButton imageButtonSearch;
    TextView txtCity, txtCountry, txtTemp, txtStatus, txtHumidity, txtCloud, txtWind, txtDay;
    ImageView imgIcon;
    String City = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        GetCurrentWeatherData("Hanoi");
        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editSearch.getText().toString();
                if (city.equals("")) {
                    City = "Hanoi";
                    GetCurrentWeatherData(City);
                } else {
                    City = city;
                    GetCurrentWeatherData(City);
                }
            }
        });
        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, NextSevenDaysActivity.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }

    public void GetCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=fb48019463ccd6351d0e30f35e16873a";
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


                            long l = Long.valueOf(day);
                            Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy");
                            String Day = simpleDateFormat.format(date);
                            txtDay.setText(Day);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/wn/" + icon + ".png").into(imgIcon);
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

    private void AnhXa() {
        editSearch = (EditText) findViewById(R.id.edittextSearch);
//        btnSearch = (Button) findViewById(R.id.buttonSearch);
        imageButtonSearch = (ImageButton) findViewById(R.id.imageButtonSearch);
        btnChangeActivity = (Button) findViewById(R.id.buttonChangeActivity);
        txtCity = (TextView) findViewById(R.id.textviewCity);
        txtCountry = (TextView) findViewById(R.id.textviewCountry);
        txtTemp = (TextView) findViewById(R.id.textviewTemp);
        txtStatus = (TextView) findViewById(R.id.textviewStatus);
        txtHumidity = (TextView) findViewById(R.id.textviewHumi);
        txtCloud = (TextView) findViewById(R.id.textviewCloud);
        txtWind = (TextView) findViewById(R.id.textviewWind);
        txtDay = (TextView) findViewById(R.id.textviewDay);
        imgIcon = findViewById(R.id.imageIcon);
    }
}