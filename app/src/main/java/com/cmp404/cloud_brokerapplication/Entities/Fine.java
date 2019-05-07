package com.cmp404.cloud_brokerapplication.Entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Fine {
    public String fineNo;
    public String licenseNo;
    public String fee;
    public String description;
    public String date;

    public Fine(JSONObject fineObject){
        try {
            fineNo = fineObject.getString("fine-no");
            licenseNo = fineObject.getString("license-no");
            fee = fineObject.getString("fee");
            description = fineObject.getString("description");
            date = fineObject.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return date + ": " + description + " - AED" + fee;
    }
}
