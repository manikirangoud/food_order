package com.mkg.orderfood.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mkg.foodorder.R;
import com.mkg.orderfood.activities.FilterActivity;
import com.mkg.orderfood.interfaces.FilterInterface;
import com.mkg.orderfood.models.Cuisine;

import java.util.ArrayList;


public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private ArrayList<Cuisine> cuisines;
    public static FilterInterface filterInterface;

    public FilterAdapter(ArrayList<Cuisine> cuisines) {
        this.cuisines = cuisines;
    }


    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_cuisines_layout, parent, false);
        return new FilterAdapter.ViewHolder(view);
    }


    public void updateData(ArrayList<Cuisine> cuisines){
        this.cuisines = cuisines;
    }


    @Override
    public void onBindViewHolder(final FilterAdapter.ViewHolder holder, final int position) {

        holder.tvCuisineName.setText(cuisines.get(position).getCuisine_name());

        //in some cases, it will prevent unwanted situations
        holder.cbCuisine.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.cbCuisine.setChecked(cuisines.get(position).isChecked());

        holder.cbCuisine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cuisines.get(position).setChecked(isChecked);
                holder.cbCuisine.setChecked(isChecked);
                FilterActivity.selectedCuisines += cuisines.get(position).isChecked() ? 1 : -1;
                filterInterface.cuisineSelectedCount(cuisines);
            }
        });

        holder.tvCuisineName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cbCuisine.setChecked(!cuisines.get(position).isChecked());
            }
        });
    }


    @Override
    public int getItemCount() {

        if(cuisines != null){
            return cuisines.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCuisineName;
        private CheckBox cbCuisine;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvCuisineName = itemView.findViewById(R.id.tv_cuisine_name);
            cbCuisine = itemView.findViewById(R.id.cb_cuisines);

        }

    }
}
