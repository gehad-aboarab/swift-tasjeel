package com.cmp404.cloud_brokerapplication.Android;

import android.app.Application;

/**
 * Created by Gehad on 5/3/2019.
 */

public class BrokerApplication extends Application {
    private String currentUserName, currentRegistrationNo, currentLicenseNo, currentCreditCard;

    public void loadProfile(String name, String registrationNo, String licenseNo, String creditCard){
        currentUserName = name;
        currentRegistrationNo = registrationNo;
        currentLicenseNo = licenseNo;
        currentCreditCard = creditCard;
    }
}
