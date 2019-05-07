package com.cmp404.cloud_brokerapplication.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gehad on 5/7/2019.
 */

public class Insurance {
    public String name;
    public String path;
    public String id;
    public ArrayList<Package> packages;

    public Insurance(JSONObject object){
        try {
            id = object.getString("id");
            name = object.getString("name");
            path = object.getString("path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        packages = new ArrayList<>();
    }

    public void addPackage(JSONObject packageObject){
        Package p = new Package(packageObject);
        packages.add(p);
    }

    public String getPackage(int i){
        return packages.get(i).packageNo;
    }

    @Override
    public String toString() {
        return name;
    }

    class Package {
        public String id;
        public String packageNo;
        public String fee;
        public String description;
        public String company;

        public Package(JSONObject object) {
            try {
                id = object.getString("id");
                packageNo = object.getString("package-no");
                fee = object.getString("fee");
                description = object.getString("description");
                company = object.getString("company");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
