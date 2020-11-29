package com.mkg.orderfood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.mkg.foodorder.R;
import com.mkg.orderfood.adapters.RestaurantsAdapter;
import com.mkg.orderfood.models.Restaurant;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Manikiran Goud on 21-02-2018.
 */

public class RestaurantDetailsFragment extends Fragment {

    private View view;
    private RecyclerView rvRestaurants;
    private ArrayList<Restaurant> restaurants;
    private RestaurantsAdapter restaurantsAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.restaurants_layout, container, false);

        rvRestaurants = view.findViewById(R.id.rv_restaurants);


        /*addRestaurants();*/

        restaurants = NearMeFragment.restaurants;

        if(restaurants != null){

            String target = ExploreFragment.targetSearch;

            ArrayList<Restaurant> tempRestaurants = new ArrayList<>();

            for (Restaurant restaurant: restaurants){
                if(restaurant.getName().toLowerCase().contains(target)){
                    tempRestaurants.add(restaurant);
                }
            }

            rvRestaurants.setLayoutManager(new LinearLayoutManager(getActivity()));
            restaurantsAdapter = new RestaurantsAdapter(getActivity(), tempRestaurants);
            rvRestaurants.setAdapter(restaurantsAdapter);
            //RestaurantsAdapter.restaurantInterface = restaurantInterface;
        }


        return view;
    }


}
