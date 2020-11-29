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

import com.mkg.foodorder.R;

/**
 * Created by Manikiran Goud on 11-02-2018.
 */

public class CartFragment extends Fragment {

    private View view;
    private LinearLayout linearLayout;
    private LayoutInflater layoutInflater;
    private SharedPreferences sharedPreferences;
    private boolean itemsAvailable = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cart_fragment_layout, container, false);

        layoutInflater = ( LayoutInflater ) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout = ( LinearLayout ) view.findViewById(R.id.ll_cart_layout);


        sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);

        itemsAvailable = sharedPreferences.getBoolean("itemsAvailable", false);

        if(itemsAvailable) {
            linearLayout.removeAllViews();
            View tempView = layoutInflater.inflate(R.layout.cart_item_layout, null);
            linearLayout.addView(tempView);
        }



        return view;
    }
}
