package com.mkg.orderfood.models;

public class Cuisine {

    private int cuisine_id;
    private String cuisine_name;
    private boolean isChecked = false;

    public Cuisine(int id, String cuisine_name) {
        this.cuisine_id = id;
        this.cuisine_name = cuisine_name;
    }

    public Cuisine() {
    }

    public int getId() {
        return cuisine_id;
    }

    public void setId(int id) {
        this.cuisine_id = id;
    }

    public String getCuisine_name() {
        return cuisine_name;
    }

    public void setCuisine_name(String cuisine_name) {
        this.cuisine_name = cuisine_name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
