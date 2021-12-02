package com.example.ejercicio05;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SetEventPage extends AppCompatActivity {

    EditText titleEventET;
    EditText placeEventET;
    Button addEventBT;

    String title;
    String place;
    String time;
    String date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_event_page);

        titleEventET = findViewById(R.id.titleEventET);
        placeEventET = findViewById(R.id.placeEventET);
        addEventBT = findViewById(R.id.addEventBT);
        //get info from another activity
        Intent intent = getIntent();
        time = intent.getStringExtra("Time");
        date = intent.getStringExtra("Date");
    }

    public void addEventTitlePlace(View view) {
        title = titleEventET.getText().toString();
        place = placeEventET.getText().toString();

        if (title.equals("") && place.equals("")) {
            Toast.makeText(this, getString(R.string.toast_title_location), Toast.LENGTH_SHORT).show();
        } else if (title.equals("")) {
            Toast.makeText(this, getString(R.string.toast_title), Toast.LENGTH_SHORT).show();
        } else if (place.equals("")) {
            Toast.makeText(this, getString(R.string.toast_location), Toast.LENGTH_SHORT).show();
        } else {
            //send info to MApsActivity and open it
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("Time", time);
            intent.putExtra("Date", date);
            intent.putExtra("Title", title);
            intent.putExtra("Place", place);
            this.startActivity(intent);
            finish();
        }
    }
}

