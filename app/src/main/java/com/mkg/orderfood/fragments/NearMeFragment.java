package com.mkg.orderfood.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.PopupMenu;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.mkg.foodorder.R;
import com.mkg.orderfood.Constants;
import com.mkg.orderfood.RestaurantMenu;
import com.mkg.orderfood.activities.FilterActivity;
import com.mkg.orderfood.activities.LocationActivity;
import com.mkg.orderfood.activities.RestaurantDetailActivity;
import com.mkg.orderfood.adapters.RestaurantsAdapter;
import com.mkg.orderfood.customviews.UbuntuTextView;
import com.mkg.orderfood.interfaces.RestaurantInterface;
import com.mkg.orderfood.models.Cuisine;
import com.mkg.orderfood.models.Restaurant;
import com.mkg.orderfood.models.RestaurantsCollection;
import com.mkg.orderfood.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import static com.mkg.orderfood.Constants.CURRENT_LAT;
import static com.mkg.orderfood.Constants.CURRENT_LONG;
import static com.mkg.orderfood.Constants.RESULT_FILTERS;
import static com.mkg.orderfood.Constants.RESULT_MAPS;
import static com.mkg.orderfood.Constants.ZOMATO_API_KEY;

/**
 * Created by Manikiran Goud on 11-02-2018.
 */

public class NearMeFragment extends Fragment implements View.OnClickListener, RestaurantInterface{

    private View view;
    private TableRow trRelevance;
    private PopupMenu popupMenu;
    private String username = "User";
    private User user;
    private String userMessage;


    static int drawables[] = {R.drawable.offer_001, R.drawable.offer_002, R.drawable.offer_003,
            R.drawable.offer_004, R.drawable.offer_005};

    private ProgressBar pbRes;
    private UbuntuTextView utvUserMessage;
    private UbuntuTextView utvNoOfRestaurants;
    private UbuntuTextView utvFilterCount;
    private UbuntuTextView utvRelevance;
    private ImageView ivFilter;
    private CoordinatorLayout clFilter;


    //Restaurant detail variables
    public static ArrayList<Restaurant> restaurants = new ArrayList<>();
    public static ArrayList<Restaurant> restaurantsFiltered;
    private RecyclerView rvRestaurants;
    private RestaurantsAdapter restaurantsAdapter;
    private RestaurantInterface restaurantInterface;


    private double currentLat = CURRENT_LAT;
    private double currentLong = CURRENT_LONG;

    private CarouselView carouselView;
    private SharedPreferences sharedPreferences;
    private ArrayList<RestaurantsCollection> resCollections = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.near_me_fragment_layout, container, false);


        rvRestaurants = view.findViewById(R.id.rv_restaurants);
        trRelevance = view.findViewById(R.id.tr_relevance);
        utvRelevance = view.findViewById(R.id.utv_relevance);
        carouselView = view.findViewById(R.id.carouselView);
        pbRes = view.findViewById(R.id.progressBar3);
        ivFilter = view.findViewById(R.id.ivFilter);
        clFilter = view.findViewById(R.id.clFilter);
        utvFilterCount = view.findViewById(R.id.utvFilterCount);
        utvUserMessage =  view.findViewById(R.id.utv_user_message);
        utvNoOfRestaurants = view.findViewById(R.id.utv_no_of_restaurants);
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);


        pbRes.setAlpha(0.0f);

        restaurantInterface = this;

       // getUser();

        username = sharedPreferences.getString(Constants.USERNAME, "User");
        userMessage = "Hello " + username + ", " + getUserMessage();
        utvUserMessage.setText(userMessage);

        //Setting the main offers
        setCollections();


        //loadRestaurants( currentLat, currentLong, 0, true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_small));
        rvRestaurants.addItemDecoration(dividerItemDecoration);



        //Setting a popup menu when Relevance clicked
        popupMenu = new PopupMenu(getActivity(), trRelevance);
        popupMenu.getMenuInflater().inflate(R.menu.relevance_menu, popupMenu.getMenu());

        trRelevance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });


        //Setting the item click listener to popup menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                utvRelevance.setText(item.getTitle());
                ArrayList<Restaurant> tempRestaurants = restaurants;

                switch (item.getItemId()){

                    case R.id.relevance:
                        break;

                    case R.id.cost_for_person:
                        Collections.sort(tempRestaurants, new Comparator<Restaurant>() {
                            @Override
                            public int compare(Restaurant lhs, Restaurant rhs) {
                                return lhs.getAverage_cost_for_two() - rhs.getAverage_cost_for_two();
                            }
                        });
                        break;

                    case R.id.timings:
                        Collections.sort(tempRestaurants, new Comparator<Restaurant>() {
                            @Override
                            public int compare(Restaurant lhs, Restaurant rhs) {
                                return lhs.getTimings().compareTo(rhs.getTimings());
                            }
                        });
                        break;

                    case R.id.rating:
                        Collections.sort(tempRestaurants, new Comparator<Restaurant>() {
                            @Override
                            public int compare(Restaurant lhs, Restaurant rhs) {
                                return lhs.getUser_rating().getAggregate_rating() < rhs.getUser_rating().getAggregate_rating() ? 0 : -1;
                            }
                        });
                        break;
                }

                if(restaurantsAdapter != null){
                    restaurantsAdapter.updateData(tempRestaurants);
                    restaurantsAdapter.notifyDataSetChanged();
                    rvRestaurants.smoothScrollToPosition(0);
                }
                return true;
            }
        });


        rvRestaurants.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    loadRestaurants(currentLat, currentLong, restaurants.size(), false);
                }
            }
        });

        //Restaurants Filters
        clFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFilterActivity = new Intent(getActivity(), FilterActivity.class);
                startActivityForResult(intentFilterActivity, RESULT_FILTERS);
            }
        });


        return view;
    }


    //Getting the user message
    private String getUserMessage() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        String userMessage = "";
        int hour = date.getHours();
        if ( hour < 3 ){
            userMessage = getResources().getString(R.string.midnight_mood);
        }else if ( hour < 12 ){
            userMessage = getResources().getString(R.string.breakfast_mood);
        } else if ( hour < 16){
            userMessage = getResources().getString(R.string.lunch_mood);
        } else if ( hour < 19) {
            userMessage = getResources().getString(R.string.snack_mood);
        } else if ( hour < 24 ){
            userMessage = getResources().getString(R.string.dinner_mood);
        }
        return userMessage;
    }


    //Getting the user details
    private void getUser() {

        //Getting user information from firebase
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users/" + Constants.USER_ID + "/" + "profileDetails");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User tempUser = snapshot.getValue(User.class);
                if(tempUser != null){

                    user = tempUser;
                    userMessage = "Hello " + user.getUsername() + ", " + getUserMessage();
                    utvUserMessage.setText(userMessage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //Displaying the offers images
    private void setCollections() {

        //Loading the Restaurant collections.
        //Sending the network request to load restaurants
        AndroidNetworking.get("https://developers.zomato.com/api/v2.1/collections")
            .addQueryParameter("lat", String.valueOf(CURRENT_LAT))
            .addQueryParameter("lon", String.valueOf(CURRENT_LONG))
            .addQueryParameter("count", String.valueOf(15))
            .addHeaders("user-key", ZOMATO_API_KEY)
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("collections");

                        if(jsonArray != null){
                            int length = jsonArray.length();
                            Gson gson = new Gson();
                            for(int i = 0; i < length ; ++i){
                                resCollections.add(gson.fromJson(jsonArray.getJSONObject(i)
                                        .getJSONObject("collection").toString(), RestaurantsCollection.class));
                            }

                            int size = resCollections.size();
                            if(size > 0){
                                //Setting the image carousel
                                carouselView.setSize(size);
                                carouselView.setResource(R.layout.collection_layout);
                                carouselView.setCarouselViewListener(new CarouselViewListener() {
                                    @Override
                                    public void onBindView(View view, int position) {
                                        // Example here is setting up a full image carousel
                                        ImageView imageView = view.findViewById(R.id.ivCollection);
                                        UbuntuTextView utvTitle = view.findViewById(R.id.utvTitle);

                                        utvTitle.setText(resCollections.get(position).getTitle());
                                        //Loading the network images
                                        Glide.with(getActivity())
                                                .load(resCollections.get(position).getImage_url())
                                                .thumbnail(0.5f)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.offer_004)
                                                .into(imageView);
                                    }
                                });
                                carouselView.show();
                            } else {
                                //There are no collection can keep the default or no content.
                                setDefaultCollections();
                            }
                        }
                    } catch (Exception e) {
                        setDefaultCollections();
                        Log.e("Data - onResponse", e.toString());
                    }
                }

                @Override
                public void onError(ANError error) {
                    setDefaultCollections();
                    Log.e("Data Error", error.toString());
                }
            });
    }


    //Setting the default restaurants collections
    private void setDefaultCollections() {
        int length = drawables.length;
        carouselView.setSize(length);
        carouselView.setResource(R.layout.collection_layout);
        carouselView.setCarouselViewListener(new CarouselViewListener() {
            @Override
            public void onBindView(View view, int position) {
                // Example here is setting up a full image carousel
                ImageView imageView = view.findViewById(R.id.ivCollection);
                UbuntuTextView utvTitle = view.findViewById(R.id.utvTitle);

                imageView.setImageResource(drawables[position]);
                utvTitle.setText("Sample Collection " + (position + 1));
            }
        });
        carouselView.show();
    }


    //Remove duplicates from the restaurants list
    public ArrayList<Restaurant>  removeDuplicates(ArrayList<Restaurant> list){
        Set<Restaurant> set = new TreeSet(new Comparator<Restaurant>() {

            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                if(o1.getId() == o2.getId()){
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(list);
        return new ArrayList<Restaurant>(set);
    }



    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), RestaurantMenu.class));
    }



    @Override
    public void onRestaurantClicked(Restaurant restaurant, View view) {

            Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
            intent.putExtra("restaurant", restaurant);

            Pair[] pairs = new Pair[2];

            ImageView resImage = view.findViewById(R.id.iv_restaurant);
            UbuntuTextView resName = view.findViewById(R.id.utv_res_name);

            pairs[0] = new Pair<View, String>(resImage, getResources().getString(R.string.restaurant_image));
            pairs[1] = new Pair<View, String>(resName, getResources().getString(R.string.toolbar_title));

            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
            startActivity(intent, activityOptions.toBundle());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_main);
        CardView cvLocation = getActivity().findViewById(R.id.cvLocation);

        try{

            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(), LocationActivity.class), RESULT_MAPS);
                }
            });

            cvLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(), LocationActivity.class), RESULT_MAPS);
                }
            });

        } catch (Exception e){
            Log.e("Error - Nearme", e.toString());
            Toast.makeText(getActivity(), "Unable to open maps for location", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_MAPS && data != null){
            double latitude = data.getDoubleExtra("latitude", CURRENT_LAT);
            double longitude = data.getDoubleExtra("longitude", CURRENT_LONG);
            loadRestaurants(latitude, longitude, 0, true);
        } else if(resultCode == RESULT_FILTERS){

            if(FilterActivity.selectedCuisines == 0){

                //Updating the UI
                utvFilterCount.setVisibility(View.GONE);

                if(restaurantsAdapter != null){
                    //Updating the data
                    utvNoOfRestaurants.setText(restaurants.size() + " " + getResources().getString(R.string.restaurants_found));
                    restaurantsAdapter.updateData(restaurants);
                    restaurantsAdapter.notifyDataSetChanged();
                }
            } else if(FilterActivity.selectedCuisines > 0) {

                //Updating the UI
                utvFilterCount.setVisibility(View.VISIBLE);
                utvFilterCount.setText(String.valueOf(FilterActivity.selectedCuisines));

                //Updating the data
                if (restaurantsFiltered.size() > 0){
                    if(restaurantsAdapter != null) {
                        utvNoOfRestaurants.setText(restaurantsFiltered.size() + " " + getResources().getString(R.string.restaurants_found));
                        restaurantsAdapter.updateData(restaurantsFiltered);
                        restaurantsAdapter.notifyDataSetChanged();
                    }
                } else{
                    loadRestaurants(CURRENT_LAT, CURRENT_LONG, 0, true);
                }
            }
        }
    }


    //Loading the restaurants
    private void loadRestaurants(double latitude, double longitude, int offset, final boolean fromNewLocation){

        //Updating the UI
        pbRes.setAlpha(1.0f);
        pbRes.setVisibility(View.VISIBLE);
        rvRestaurants.setAlpha(0.3f);

        //Displaying the location name
        displayLocationName(latitude, longitude);

        //Fetching any filters if user selected in Filters Activity
        String cuisinesId = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (FilterActivity.selectedCuisines > 0 && FilterActivity.cuisines != null){
            for(Cuisine cuisine: FilterActivity.cuisines){
                if (cuisine.isChecked()){
                    stringBuilder.append(cuisine.getId()).append(",");
                }
            }
            cuisinesId = stringBuilder.toString().substring(0, stringBuilder.length()-1);
        }


        //Sending the network request to load restaurants
        AndroidNetworking.get("https://developers.zomato.com/api/v2.1/search")
                .addQueryParameter("lat", String.valueOf(latitude))
                .addQueryParameter("lon", String.valueOf(longitude))
                .addQueryParameter("sort", "real_distance")
                .addQueryParameter("radius", "15000")
                .addQueryParameter("start", Integer.toString(offset))
                .addQueryParameter("cuisines", cuisinesId)
                .addHeaders("user-key", ZOMATO_API_KEY)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("restaurants");

                            if(jsonArray != null){
                                int length = jsonArray.length();
                                Gson gson = new Gson();

                                if(fromNewLocation){
                                    restaurants.clear();
                                }
                                for(int i = 0; i < length ; ++i){
                                    restaurants.add(gson.fromJson(jsonArray.getJSONObject(i).getJSONObject("restaurant").toString(), Restaurant.class));
                                }

                                rvRestaurants.setAlpha(1.0f);
                                if(restaurants.size() > 0){
                                    pbRes.setAlpha(0.0f);
                                    if(restaurantsAdapter == null){
                                        rvRestaurants.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        restaurantsAdapter = new RestaurantsAdapter(getActivity(), restaurants);
                                        rvRestaurants.setAdapter(restaurantsAdapter);
                                        RestaurantsAdapter.restaurantInterface = restaurantInterface;
                                    } else {
                                        restaurants = removeDuplicates(restaurants);
                                        restaurantsAdapter.updateData(restaurants);
                                        restaurantsAdapter.notifyDataSetChanged();
                                    }
                                    //Setting number of restaurants found
                                    utvNoOfRestaurants.setText(restaurants.size() + " " + getResources().getString(R.string.restaurants_found));
                                } else{
                                    utvNoOfRestaurants.setText(getResources().getString(R.string.zero_restaurants_found));
                                }

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


    //For displaying the location name in the toolbar title
    private void displayLocationName(double latitude, double longitude) {
        try {
            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0 && getActivity()!= null) {
                UbuntuTextView utvToolbarTitle = getActivity().findViewById(R.id.utv_toolbar_title);
                utvToolbarTitle.setText(addresses.get(0).getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
