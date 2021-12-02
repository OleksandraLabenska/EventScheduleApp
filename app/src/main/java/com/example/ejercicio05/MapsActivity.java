package com.example.ejercicio05;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.google.android.gms.maps.model.MarkerOptions.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private GoogleMap map;

    ListView list;
    ListEvents listEvents;
    SharedPreferences sharedPreferences;
    Adaptor adaptor;
    Geocoder geocoder;

    String time;
    String date;
    String title;
    String place;
    String timeDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        String json = sharedPreferences.getString("Events", "");

        if (!json.isEmpty()) {
            listEvents = new ListEvents();
            listEvents = listEvents.fromJson(json);
        } else {
            listEvents = new ListEvents();
        }

        list = findViewById(R.id.list);

        adaptor = new Adaptor(this, R.layout.event, listEvents.events);
        list.setAdapter(adaptor);

        list.setOnItemClickListener(this);
        //every time mapsActivity is opened, I call this method to print out all created events in the list
        refreshData();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //set start the position of the camera
        LatLng startLocality = new LatLng(40.4, -3.7);
        map.moveCamera(CameraUpdateFactory.newLatLng(startLocality));
        //asking user about permission to use location
        activateLocation();

        //show the list of the event on the map(markers)
        geocode();
    }

    //this function print all the locations of the events, saved in the list of  event, so every time when I call it, it will update and  add new locations
    public void geocode() {
        geocoder = new Geocoder(this);
        try {
            for (int i = 0; i < listEvents.events.size(); i++) {
                //get the location from the list
                String placeName = listEvents.events.get(i).place;
                String title = listEvents.events.get(i).title;
                String time = listEvents.events.get(i).date;
                List<Address> directions = geocoder.getFromLocationName(placeName, 1);
                if (directions.size() != 0) {
                    Address direction = directions.get(0);
                    //convert the string location to latlng
                    LatLng location = new LatLng(direction.getLatitude(), direction.getLongitude());
                    //add the marker
                    map.addMarker(new MarkerOptions().position(location).title(title).snippet(time));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(location)
                            .zoom(7)                   // Sets the zoom
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else if(directions.size() == 0){
                    Toast.makeText(this, "The requested address " + placeName +" was not found", Toast.LENGTH_SHORT).show();
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this function receive all the extras from another activities, and update the list of the events
    public void refreshData() {
        Intent intent = getIntent();
        time = intent.getStringExtra("Time");
        date = intent.getStringExtra("Date");
        title = intent.getStringExtra("Title");
        place = intent.getStringExtra("Place");
        timeDate = time + "   " + date;
        Event newEvent = new Event(title, timeDate, place);

        if (place != null) {
            listEvents.events.add(newEvent);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Events", listEvents.toJson());
            editor.apply();

            adaptor.notifyDataSetChanged();
        }
    }


    public void addNewEvent(View view) {
        Intent intent = new Intent(this, PickDatePage.class);
        this.startActivity(intent);
        finish();
    }


    //by clicking on the item, user will delete it. also I call refreshMap() function to update the map and delete the marker
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listEvents.events.remove(position);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Events", listEvents.toJson());
        editor.apply();

        adaptor.notifyDataSetChanged();
        refreshMap();
    }

    //update the map by starting maps activity again
    public void refreshMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        this.startActivity(intent);
        finish();
    }


    public void activateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 123);
            return;
        }
        map.setMyLocationEnabled(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            activateLocation();
        }
    }
}

