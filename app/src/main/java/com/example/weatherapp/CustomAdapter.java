package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Weather> arrayList;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.listview, null);

        Weather weather = arrayList.get(position);

        TextView txtDay = (TextView) view.findViewById(R.id.textviewDay2);
        TextView txtStatus = (TextView) view.findViewById(R.id.textviewStatus2);
        TextView txtMaxTemp = (TextView) view.findViewById(R.id.textviewMaxTemp);
        TextView txtMinTemp = (TextView) view.findViewById(R.id.textviewMinTemp);
        ImageView imageViewStatus = (ImageView) view.findViewById(R.id.imageviewStatus);

        txtDay.setText(weather.Day);
        txtStatus.setText(weather.Status);
        txtMaxTemp.setText(weather.MaxTemp + "ﾟC");
        txtMinTemp.setText(weather.MinTemp + "ﾟC");

        Picasso.with(context).load("http://openweathermap.org/img/wn/" + weather.Image + ".png").into(imageViewStatus);

        return view;
    }
}
