package com.mkg.orderfood.models;

public class RestaurantsCollection {

    private int collection_id;
    private int res_count;
    private String image_url;
    private String url;
    private String title;
    private String description;
    private String share_url;

    public RestaurantsCollection() {
    }


    public int getCollection_id() {
        return collection_id;
    }

    public int getRes_count() {
        return res_count;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getShare_url() {
        return share_url;
    }
}
