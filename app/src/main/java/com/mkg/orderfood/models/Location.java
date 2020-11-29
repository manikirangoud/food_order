package com.mkg.orderfood.models;

import java.io.Serializable;

public class Location implements Serializable {

    private String address;
    private String locality;
    private String city;


    public Location() {
    }

    public String getAddress() {
        return address;
    }

    public String getLocality() {
        return locality;
    }

    public String getCity() {
        return city;
    }
}
