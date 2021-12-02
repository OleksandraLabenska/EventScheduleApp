package com.example.ejercicio05;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class PickDatePage extends AppCompatActivity implements TimePicker.OnTimeChangedListener, DatePicker.OnDateChangedListener {
    TimePicker timeTP;
    DatePicker dateDP;
    Calendar calendar;
    Button addEventLocationBT;

    String time;
    String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date_page);

        timeTP = findViewById(R.id.timeTP);
        dateDP = findViewById(R.id.dateDP);
        addEventLocationBT = findViewById(R.id.addEventLocationBT);

        timeTP.setOnTimeChangedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateDP.setOnDateChangedListener(this);
        }

        calendar = new GregorianCalendar();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        time = hourOfDay + " : " + minute;
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);

        date = dayOfMonth + "/" + monthOfYear + "/" + year;
    }

    public void addEventDate(View view) {
        if (time == null && date == null) {
            Toast.makeText(this, getString(R.string.toast_date_time), Toast.LENGTH_SHORT).show();
        } else if (date == null) {
            Toast.makeText(this, getString(R.string.toast_date), Toast.LENGTH_SHORT).show();
        } else if (time == null) {
            Toast.makeText(this, getString(R.string.toast_time), Toast.LENGTH_SHORT).show();
        } else {
            //send info and open another activity
            Intent intent = new Intent(this, SetEventPage.class);
            intent.putExtra("Time", time);
            intent.putExtra("Date", date);
            this.startActivity(intent);
            finish();
        }
    }
}
