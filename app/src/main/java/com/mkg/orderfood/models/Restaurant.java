package com.mkg.orderfood.models;

import java.io.Serializable;

public class Restaurant implements Serializable {

    private int id;
    private String name;
    private String url;
    private int average_cost_for_two;
    private UserRating user_rating;
    private String photos_url;
    private String thumb;
    private String timings;
    private Location location;
    private int is_delivering_now;
    private int all_reviews_count;
    private String cuisines;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getAverage_cost_for_two() {
        return average_cost_for_two;
    }


    public UserRating getUser_rating() {
        return user_rating;
    }


    public String getPhotos_url() {
        return photos_url;
    }


    public String getThumb() {
        return thumb;
    }



    public String getTimings() {
        return timings;
    }

    public Location getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }

    public int is_delivering_now() {
        return is_delivering_now;
    }


    public int getIs_delivering_now() {
        return is_delivering_now;
    }

    public int getAll_reviews_count() {
        return all_reviews_count;
    }

    public String getCuisines() {
        return cuisines;
    }
}
