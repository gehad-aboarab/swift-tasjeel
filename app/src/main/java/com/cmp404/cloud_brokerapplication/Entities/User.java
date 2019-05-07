package com.cmp404.cloud_brokerapplication.Entities;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String name;
    public String contact;
    public String licensePlate;
    public String registrationNo;
    public String creditCard;
    public String insurancePackageRef;
    public String testingCenterRef;
    public boolean paidFines;

    public User(JSONObject object){
        try {
            name = object.getString("name");
            contact = object.getString("email");
            licensePlate = object.getString("license-no");
            registrationNo = object.getString("registration-no");
            creditCard = object.getString("credit-card");
            paidFines = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
