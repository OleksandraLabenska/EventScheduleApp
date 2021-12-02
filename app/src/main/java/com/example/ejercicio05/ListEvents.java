package com.example.ejercicio05;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ListEvents {

    public ArrayList<Event> events;

    public ListEvents() {
        events = new ArrayList<>();
    }


    public String toJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public ListEvents fromJson(String json) {
        Gson gson = new Gson();
        ListEvents listEvents = gson.fromJson(json, ListEvents.class);
        return listEvents;
    }
}
