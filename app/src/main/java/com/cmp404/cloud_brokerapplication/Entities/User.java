package com.cmp404.cloud_brokerapplication.Entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gehad on 5/7/2019.
 */

public class User {
    public String name;
    public String contact;
    public String licensePlate;
    public String registrationNo;
    public String creditCard;
    public String insurancePackageRef;

    public User(JSONObject object){
        try {
            name = object.getString("name");
            contact = object.getString("email");
            licensePlate = object.getString("license-no");
            registrationNo = object.getString("registration-no");
            creditCard = object.getString("credit-card");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
