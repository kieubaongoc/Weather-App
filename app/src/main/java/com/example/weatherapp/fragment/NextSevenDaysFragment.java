package com.example.weatherapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.example.weatherapp.CustomAdapter;
import com.example.weatherapp.R;
import com.example.weatherapp.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NextSevenDaysFragment extends Fragment {
    private View mView;
    TextView txtName;
    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<Weather> weatherArrayList;

    public NextSevenDaysFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_activity_nextsevendays, container, false);
        AnhXa();
        String getCity = getArguments().getString("key");
        if (getCity.equals("")) {
            getCity = "Hanoi";
            Get7DaysData(getCity);
        } else {
            Get7DaysData(getCity);
        }
        return mView;
    }

    private void Get7DaysData(String data) {
        // API key update 3 hours
//        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&cnt=7&appid" +
//                "=7f52a68ab01932a3bd252c897d09192a&units=metric";
        String url = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + data + "&units=metric&cnt=7&appid=53fbf527d52d4d773e828243b90c1f8e";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE\ndd/MM/yyyy");
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
        txtName = (TextView) mView.findViewById(R.id.textviewCityName);
        listView = (ListView) mView.findViewById(R.id.listView);
        weatherArrayList = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(getActivity().getApplicationContext(), weatherArrayList);
        listView.setAdapter(customAdapter);
    }

//    public void receiveDataFromHomeFragment(String City) {
//        Get7DaysData(City);
//    }
}
