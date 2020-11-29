package com.mkg.orderfood.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mkg.foodorder.R;
import com.mkg.orderfood.customviews.UbuntuTextView;
import com.mkg.orderfood.models.Restaurant;


import org.json.JSONObject;

import static com.mkg.orderfood.Constants.ZOMATO_API_KEY;


public class RestaurantDetailActivity extends AppCompatActivity {


    private Restaurant restaurant;

    private CardView cvFabBack;
    private ImageView ivRestaurant;
    private UbuntuTextView utvToolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_details);


        cvFabBack = findViewById(R.id.cv_fab_back);
        ivRestaurant = findViewById(R.id.iv_restaurant);
        utvToolbarTitle = findViewById(R.id.utv_toolbar_title);


        restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");

        if (restaurant.getId() > 0) {
            loadRestaurantMenu(restaurant.getId());


            utvToolbarTitle.setText(restaurant.getName());
        }


        Glide.with(this)
                .load(restaurant.getThumb())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.offer_001)
                .into(ivRestaurant);


        cvFabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void loadRestaurantMenu(int id) {

        AndroidNetworking.get("https://developers.zomato.com/api/v2.1/dailymenu")
                .addQueryParameter("res_id", Integer.toString(id))
                .addHeaders("user-key", ZOMATO_API_KEY)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //JSONArray jsonArray = response.getJSONArray("restaurants");

                            if(response != null){
                               Log.i("Menu ", response.toString());

                            }
                        } catch (Exception e) {
                            Log.e("Data - onResponse", e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("Data Error", error.toString());
                    }
                });
    }



}
