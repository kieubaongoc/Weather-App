package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

public class NextSevenDaysActivity extends AppCompatActivity {

    String cityName = "";
    ImageView imageviewBack;
    ImageButton imageButtonBack;
    TextView txtName;
    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<Weather> weatherArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_seven_days);
        AnhXa();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
//        Log.d("ketqua", "Du lieu truyen qua " + city);
        if (city.equals("")) {
            cityName = "Hanoi";
            Get7DaysData(cityName);
        } else {
            cityName = city;
            Get7DaysData(cityName);
        }
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(NextSevenDaysActivity.this, MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });
    }

    private void Get7DaysData(String data) {
        // API key update 3 hours
//        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&cnt=7&appid" +
//                "=7f52a68ab01932a3bd252c897d09192a&units=metric";
        String url = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + data + "&units=metric&cnt=7&appid=53fbf527d52d4d773e828243b90c1f8e";
        RequestQueue requestQueue = Volley.newRequestQueue(NextSevenDaysActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("ketqua", "Json: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            txtName.setText(name);

                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String ngay = jsonObjectList.getString("dt");

                                long l = Long.valueOf(ngay);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd/MM/yyyy");
                                String Day = simpleDateFormat.format(date);
                                Log.d("ketqua", "Datetime: " + date);

                                // Json key-value update 3 hours
//                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
//                                String max = jsonObjectTemp.getString("temp_max");
//                                String min = jsonObjectTemp.getString("temp_min");

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                String max = jsonObjectTemp.getString("max");
                                String min = jsonObjectTemp.getString("min");

                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);
                                String maxTemp = String.valueOf(a.intValue());
                                String minTemp = String.valueOf(b.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                weatherArrayList.add(new Weather(Day, status, icon, maxTemp, minTemp));
                            }
                            customAdapter.notifyDataSetChanged();
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
//        imageviewBack = (ImageView) findViewById(R.id.imageviewBack);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        txtName = (TextView) findViewById(R.id.textviewCityName);
        listView = (ListView) findViewById(R.id.listView);
        weatherArrayList = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(NextSevenDaysActivity.this, weatherArrayList);
        listView.setAdapter(customAdapter);
    }
}