package com.cmp404.cloud_brokerapplication.Entities;

import com.cmp404.cloud_brokerapplication.Android.BrokerApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private BrokerApplication application;

    private String name;
    private String contact;
    private String licensePlate;
    private String registrationNo;
    private String creditCard;
    private String insurancePackageRef;
    private String testingCenterRef;
    private String cvv;
    private boolean insuranceDone;
    private boolean bookingDone;
    private boolean paidFines;
    private boolean paidRenewal;

    public User(BrokerApplication application, JSONObject object){
        this.application = application;

        try {
            name = object.getString("name");
            contact = object.getString("email");
            licensePlate = object.getString("license-no");
            registrationNo = object.getString("registration-no");
            creditCard = object.getString("credit-card");
            insuranceDone = object.getBoolean("insurance");
            bookingDone = object.getBoolean("testing");
            paidFines = object.getBoolean("fines");
            paidRenewal = object.getBoolean("renewal");
            insurancePackageRef = object.getString("insurance-ref");
            testingCenterRef = object.getString("testing-ref");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        application.database.updateUserStrings("name", name);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
        application.database.updateUserStrings("email", contact);
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
        application.database.updateUserStrings("license-no", licensePlate);
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
        application.database.updateUserStrings("registration-no", registrationNo);
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
        application.database.updateUserStrings("credit-card", creditCard);
    }

    public String getInsurancePackageRef() {
        return insurancePackageRef;
    }

    public void setInsurancePackageRef(String insurancePackageRef) {
        this.insurancePackageRef = insurancePackageRef;
        application.database.updateUserStrings("insurance-ref", insurancePackageRef);
    }

    public String getTestingCenterRef() {
        return testingCenterRef;
    }

    public void setTestingCenterRef(String testingCenterRef) {
        this.testingCenterRef = testingCenterRef;
        application.database.updateUserStrings("testing-ref", testingCenterRef);
    }

    public boolean isInsuranceDone() {
        return insuranceDone;
    }

    public void setInsuranceDone(boolean insuranceDone) {
        this.insuranceDone = insuranceDone;
        application.database.updateUserBooleans("insurance", insuranceDone);
    }

    public boolean isBookingDone() {
        return bookingDone;
    }

    public void setBookingDone(boolean bookingDone) {
        this.bookingDone = bookingDone;
        application.database.updateUserBooleans("testing", bookingDone);
    }

    public boolean isPaidFines() {
        return paidFines;
    }

    public void setPaidFines(boolean paidFines) {
        this.paidFines = paidFines;
        application.database.updateUserBooleans("fines", paidFines);
    }

    public boolean isPaidRenewal() {
        return paidRenewal;
    }

    public void setPaidRenewal(boolean paidRenewal) {
        this.paidRenewal = paidRenewal;
        application.database.updateUserBooleans("renewal", paidRenewal);
    }
}
