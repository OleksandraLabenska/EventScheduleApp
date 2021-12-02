package com.example.ejercicio05;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Adaptor extends ArrayAdapter {

    Context context;
    int eventLayout;
    ArrayList<Event> events;

    public Adaptor(@NonNull Context context, int resource, @NonNull ArrayList<Event> objects) {
        super(context, resource, objects);
        this.context = context;
        eventLayout = resource;
        events = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(eventLayout, parent, false);
        }

        TextView nameEventTV = convertView.findViewById(R.id.nameEventTV);
        TextView dateEventTV = convertView.findViewById(R.id.dateEventTV);
        TextView mapTV = convertView.findViewById(R.id.mapTV);

        nameEventTV.setText(events.get(position).title);
        dateEventTV.setText(events.get(position).date);
        mapTV.setText(events.get(position).place);


        notifyDataSetChanged();

        return convertView;
    }
}
