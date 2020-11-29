package com.mkg.orderfood.models;

public class User {

    private String countryCode;
    private String mobileNumber;
    private String username;
    private String emailId;

    public User() {
    }

    public User(String countryCode, String mobileNumber, String username, String emailId) {
        this.countryCode = countryCode;
        this.mobileNumber = mobileNumber;
        this.username = username;
        this.emailId = emailId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
