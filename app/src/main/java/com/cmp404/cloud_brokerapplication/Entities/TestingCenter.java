package com.cmp404.cloud_brokerapplication.Entities;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class TestingCenter {
    public String name;
    public String path;
    public String id;
    public ArrayList<Timing> timings;

    public TestingCenter(JSONObject object){
        try {
            id = object.getString("id");
            name = object.getString("name");
            path = object.getString("path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        timings = new ArrayList<>();
    }

    public void addTiming(JSONObject timingObject){
        Timing t = new Timing(timingObject);
        timings.add(t);
    }

    public Timing getTiming(int i){
        return timings.get(i);
    }

    @Override
    public String toString() {
        return name;
    }

    public class Timing {
        public String id;
        public String date;
        public String time;
        public String company;
        public String location;

        public Timing(JSONObject object) {
            try {
                id = object.getString("timing-id");
                date = object.getString("date");
                time = object.getString("time");
                location = object.getString("location");
                company = object.getString("company");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
