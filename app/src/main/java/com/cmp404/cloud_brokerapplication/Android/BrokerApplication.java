package com.cmp404.cloud_brokerapplication.Android;

import android.app.Application;

import com.cmp404.cloud_brokerapplication.Helpers.DatabaseHelper;

public class BrokerApplication extends Application {
    public String currentUserName, currentEmail, currentRegistrationNo, currentLicenseNo, currentCreditCard;
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
