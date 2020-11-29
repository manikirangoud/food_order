package com.mkg.orderfood.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mkg.foodorder.R;
import com.mkg.orderfood.adapters.ViewPagerAdapter;
import com.mkg.orderfood.customviews.UbuntuTextView;

/**
 * Created by Manikiran Goud on 11-02-2018.
 */

public class ExploreFragment extends Fragment {


    private View view;
    private EditText editTextExplore;
    private ImageView imageViewClear;
    private LinearLayout linearLayoutMain;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    //Tooglers
    private UbuntuTextView utvRestaurants;
    private UbuntuTextView utvDishes;
    private boolean isRestaurantsSelected = true;
    private boolean isDishesSelected = false;


    public static String targetSearch = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.explore_fragment_layout, container, false);

        editTextExplore = view.findViewById(R.id.edit_text_explore);
        imageViewClear = view.findViewById(R.id.image_view_clear);
        linearLayoutMain = view.findViewById(R.id.ll_fragments_explore);
        tabLayout = view.findViewById(R.id.tlExplore);
        viewPager = view.findViewById(R.id.vpExplore);
        utvRestaurants = view.findViewById(R.id.utvRestaurantsToggle);
        utvDishes = view.findViewById(R.id.utvDishesToggle);
        sharedPreferences = getActivity().getSharedPreferences("explore_active", Context.MODE_PRIVATE);


        //Setting Togglers state
        utvRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isRestaurantsSelected = !isRestaurantsSelected;
                if(isRestaurantsSelected) {
                    utvRestaurants.setBackground(getResources().getDrawable(R.drawable.text_selected_bg));
                    utvRestaurants.setTextColor(getResources().getColor(R.color.colorTextPrimary));
                } else{
                    utvRestaurants.setBackground(getResources().getDrawable(R.drawable.rounded_bg));
                    utvRestaurants.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });


        utvDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDishesSelected = !isDishesSelected;
                if(isDishesSelected) {
                    utvDishes.setBackground(getResources().getDrawable(R.drawable.text_selected_bg));
                    utvDishes.setTextColor(getResources().getColor(R.color.colorTextPrimary));
                } else{
                    utvDishes.setBackground(getResources().getDrawable(R.drawable.rounded_bg));
                    utvDishes.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });


        editTextExplore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >= 1) {
                    imageViewClear.setVisibility(View.VISIBLE);
                    /*editor = sharedPreferences.edit();
                    editor.putString("content", s.toString());
                    editor.apply();*/
                    targetSearch = s.toString();
                    getDetails();
                } else {
                    imageViewClear.setVisibility(View.INVISIBLE);
                    clearDetails();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        imageViewClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editTextExplore.getText())) {
                    editTextExplore.setText("");
                    imageViewClear.setVisibility(View.INVISIBLE);
                }
            }
        });

        return view;
    }


    private void clearDetails() {
        if(linearLayoutMain.getChildCount() > 0) {
            linearLayoutMain.setVisibility(View.GONE);
        }
    }

    private void getDetails() {

        clearDetails();
        linearLayoutMain.setVisibility(View.VISIBLE);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        if(isRestaurantsSelected){
            viewPagerAdapter.addFragments(new RestaurantDetailsFragment(), getResources().getString(R.string.restaurants));
        }
        if(isDishesSelected){
            viewPagerAdapter.addFragments(new DishesFragment(), getResources().getString(R.string.dishes));
        }
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
