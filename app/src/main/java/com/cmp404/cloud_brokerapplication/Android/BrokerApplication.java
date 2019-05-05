package com.cmp404.cloud_brokerapplication.Android;

import android.app.Application;

import com.cmp404.cloud_brokerapplication.Helpers.DatabaseHelper;

/**
 * Created by Gehad on 5/3/2019.
 */

public class BrokerApplication extends Application {
    private String currentUserName, currentEmail, currentRegistrationNo, currentLicenseNo, currentCreditCard;
    public DatabaseHelper database;

    public BrokerApplication(){
    }

    public void initDB(){
        database = new DatabaseHelper();
    }

    public void loadProfile(String name, String email, String registrationNo, String licenseNo, String creditCard){
        currentUserName = name;
        currentEmail = email;
        currentRegistrationNo = registrationNo;
        currentLicenseNo = licenseNo;
        currentCreditCard = creditCard;
    }
}
