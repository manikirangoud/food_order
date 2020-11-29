package com.mkg.orderfood.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mkg.foodorder.R;
import com.mkg.orderfood.activities.FilterActivity;
import com.mkg.orderfood.customviews.UbuntuTextView;
import com.mkg.orderfood.interfaces.RestaurantInterface;
import com.mkg.orderfood.models.Restaurant;

import java.util.ArrayList;


/**
 * Created by Manikiran Goud on 11-02-2018.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private ArrayList<Restaurant> restaurants;
    private Context context;
    public static RestaurantInterface restaurantInterface;


    public RestaurantsAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }


    public void updateData(ArrayList<Restaurant> restaurants){
        this.restaurants = restaurants;
    }

    @Override
    public RestaurantsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RestaurantsAdapter.ViewHolder holder, final int position) {

        holder.utvResName.setText(restaurants.get(position).getName());
        int costPerPerson = restaurants.get(position).getAverage_cost_for_two() / 2;
        holder.utvCostPerPerson.setText(costPerPerson + " Per Person");

        Glide.with(context)
                .load(restaurants.get(position).getThumb())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.offer_001)
                .into(holder.ivRestaurant);

        String ratingRaters = restaurants.get(position).getUser_rating().getAggregate_rating() + " (" +
                restaurants.get(position).getUser_rating().getVotes() + ")";
        holder.utvRating.setText(ratingRaters);
        holder.utvTime.setText(restaurants.get(position).getTimings());
        holder.utvAddress.setText(restaurants.get(position).getLocation().getAddress());

        holder.llRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantInterface.onRestaurantClicked(restaurants.get(position), holder.itemView);
            }
        });

        if(FilterActivity.selectedCuisines > 0){
            holder.utvCuisines.setVisibility(View.VISIBLE);
            holder.utvCuisines.setText(restaurants.get(position).getCuisines());
        } else {
            holder.utvCuisines.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return restaurants == null ? 0 : restaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private UbuntuTextView utvResName;
        private UbuntuTextView utvCostPerPerson;
        private UbuntuTextView utvRating;
        private UbuntuTextView utvTime;
        private UbuntuTextView utvAddress;
        private UbuntuTextView utvCuisines;
        private ImageView ivRestaurant;
        private LinearLayout llRestaurant;

        public ViewHolder(View itemView) {
            super(itemView);

            utvResName = itemView.findViewById(R.id.utv_res_name);
            utvCostPerPerson = itemView.findViewById(R.id.utv_cost_per_person);
            utvRating = itemView.findViewById(R.id.utv_rating);
            utvTime = itemView.findViewById(R.id.utv_time);
            utvAddress = itemView.findViewById(R.id.utv_address);
            ivRestaurant = itemView.findViewById(R.id.iv_restaurant);
            llRestaurant = itemView.findViewById(R.id.ll_restaurant);
            utvCuisines = itemView.findViewById(R.id.utvCuisines);
        }

    }
}
