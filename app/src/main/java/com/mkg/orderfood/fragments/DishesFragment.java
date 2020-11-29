package com.mkg.orderfood.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkg.foodorder.R;

/**
 * Created by Manikiran Goud on 21-02-2018.
 */

public class DishesFragment extends Fragment {


    private View view;
    private LinearLayout linearLayout;
    private LayoutInflater layoutInflater;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.dishes_layout, container, false);

        linearLayout = ( LinearLayout ) view.findViewById(R.id.ll_dishes);

        layoutInflater = ( LayoutInflater ) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sharedPreferences = getActivity().getSharedPreferences("explore_active", Context.MODE_PRIVATE);

        for(int i = 0; i < 7; i++) {
            View tempView = layoutInflater.inflate(R.layout.dishes_row_layout, null);
            TextView textView = ( TextView ) tempView.findViewById(R.id.tv_dishes);
            textView.setText(sharedPreferences.getString("content", "dish"));
            linearLayout.addView(tempView);
        }


        return view;
    }
}
