package com.mkg.orderfood.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.mkg.foodorder.R;
import com.mkg.orderfood.Constants;
import com.mkg.orderfood.adapters.FilterAdapter;
import com.mkg.orderfood.customviews.UbuntuTextView;
import com.mkg.orderfood.fragments.NearMeFragment;
import com.mkg.orderfood.interfaces.FilterInterface;
import com.mkg.orderfood.models.Cuisine;
import com.mkg.orderfood.models.Restaurant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mkg.orderfood.Constants.RESULT_FILTERS;
import static com.mkg.orderfood.Constants.ZOMATO_API_KEY;

/**
 * Created by Manikiran Goud on 14-02-2018.
 */

public class FilterActivity extends AppCompatActivity implements FilterInterface {

    private String[] cuisinesString;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayoutCuisines;
    private Toolbar toolbar;
    private UbuntuTextView utvApplyFilters;
    private UbuntuTextView mtvSelectedCuisines;

    public static int selectedCuisines = 0;
    public static ArrayList<Cuisine> cuisines = new ArrayList<>();


    private FilterAdapter filterAdapter;
    private RecyclerView rvCuisines;
    private ProgressBar pbCuisines;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.filter_layout);


        layoutInflater = ( LayoutInflater ) getSystemService(LAYOUT_INFLATER_SERVICE);
        toolbar = findViewById(R.id.toolbar_filter_layout);
        utvApplyFilters = findViewById(R.id.utvApplyFilters);
        mtvSelectedCuisines = findViewById(R.id.utv_selected_cuisines);
        pbCuisines = findViewById(R.id.pbCuisines);

        setSupportActionBar(toolbar);


        cuisinesString = getResources().getStringArray(R.array.cuisines);

        if(cuisines.size() == 0){
            loadCuisines();
        }

        setSelectedCount();

        rvCuisines = findViewById(R.id.rv_cuisines);
        filterAdapter = new FilterAdapter(cuisines);
        rvCuisines.setLayoutManager(new LinearLayoutManager(this));
        rvCuisines.setAdapter(filterAdapter);


        FilterAdapter.filterInterface = this;

        utvApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NearMeFragment.restaurantsFiltered = new ArrayList<>();
                for(Cuisine cuisine: cuisines){
                    if(cuisine.isChecked()) {
                        for (Restaurant restaurant : NearMeFragment.restaurants) {
                            if (restaurant.getCuisines().toLowerCase().contains(cuisine.getCuisine_name().toLowerCase().trim())) {
                                NearMeFragment.restaurantsFiltered.add(restaurant);
                            }
                        }
                    }
                }
                setResult(RESULT_FILTERS);
                finish();
            }
        });
    }


    //Loading all cuisines in the area
    private void loadCuisines() {
        pbCuisines.setVisibility(View.VISIBLE);

        AndroidNetworking.get("https://developers.zomato.com/api/v2.1/cuisines")
                .addQueryParameter("lat", String.valueOf(Constants.CURRENT_LAT))
                .addQueryParameter("lon", String.valueOf(Constants.CURRENT_LONG))
                .addHeaders("user-key", ZOMATO_API_KEY)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("cuisines");

                            if(jsonArray != null){
                                int length = jsonArray.length();
                                Gson gson = new Gson();

                                for(int i = 0; i < length ; ++i){
                                    cuisines.add(gson.fromJson(jsonArray.getJSONObject(i).getJSONObject("cuisine").toString(), Cuisine.class));
                                }

                                if(cuisines.size() > 0){
                                    pbCuisines.setVisibility(View.GONE);
                                    filterAdapter.updateData(cuisines);
                                    filterAdapter.notifyDataSetChanged();
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


    @Override
    public void cuisineSelectedCount(ArrayList<Cuisine> cuisines) {

        setSelectedCount();
        this.cuisines = cuisines;
    }

    //Setting the selected count
    private void setSelectedCount() {

        if (selectedCuisines == 0) {
            mtvSelectedCuisines.setText("");
        } else if (selectedCuisines == 1) {
            mtvSelectedCuisines.setText(selectedCuisines + " Cuisine selected");
        } else if (selectedCuisines > 1) {
            mtvSelectedCuisines.setText(selectedCuisines + " Cuisines selected");
        }
    }
}
