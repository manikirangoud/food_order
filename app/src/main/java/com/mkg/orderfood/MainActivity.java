package com.mkg.orderfood;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mkg.orderfood.activities.LocationActivity;
import com.mkg.orderfood.adapters.ViewPagerAdapter;
import com.mkg.orderfood.fragments.AccountFragment;
import com.mkg.orderfood.fragments.CartFragment;
import com.mkg.orderfood.fragments.ExploreFragment;
import com.mkg.orderfood.fragments.LoginScreenFragment;
import com.mkg.orderfood.fragments.NearMeFragment;

import com.mkg.foodorder.R;

import static com.mkg.orderfood.Constants.EMAIL_ID;
import static com.mkg.orderfood.Constants.MOBILE_NUMBER;
import static com.mkg.orderfood.Constants.RESULT_MAPS;
import static com.mkg.orderfood.Constants.SHARED_PREFS;
import static com.mkg.orderfood.Constants.USERNAME;


public class MainActivity extends AppCompatActivity {


    private static final int VIEWPAGER_OFF_SCREEN_PAGE_LIMIT = 4;


    private CardView cvLogout;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;


    private ViewPager vpMain;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;


    private SharedPreferences sharedPreferences;
    private String username;
    private Toolbar toolbar;
    private CardView cvLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabLayout = findViewById(R.id.tab_layout_main);
        vpMain = findViewById(R.id.vpMain);
        cvLogout = findViewById(R.id.cvLogout);
        toolbar = findViewById(R.id.toolbar_main);
        cvLocation = findViewById(R.id.cvLocation);


        //Fetching the shared prefs
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(USERNAME, "");



        //Setting viewpager fragments
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new NearMeFragment(), getResources().getString(R.string.near_me));
        viewPagerAdapter.addFragments(new ExploreFragment(), getResources().getString(R.string.explore));
        viewPagerAdapter.addFragments(new CartFragment(), getResources().getString(R.string.cart));
        Fragment fragment = new LoginScreenFragment();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            Constants.USER_ID = firebaseUser.getUid();
            fragment = new AccountFragment();
            cvLogout.setVisibility(View.VISIBLE);
        }

        viewPagerAdapter.addFragments(fragment, getResources().getString(R.string.account));
        vpMain.setAdapter(viewPagerAdapter);
        vpMain.setOffscreenPageLimit(VIEWPAGER_OFF_SCREEN_PAGE_LIMIT);
        //vpMain.setCurrentItem(4);

        tabLayout.setupWithViewPager(vpMain);



        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, LocationActivity.class), RESULT_MAPS);
            }
        });

        cvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, LocationActivity.class), RESULT_MAPS);
            }
        });



        //Setting listener on logout button
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setIcon(getResources().getDrawable(R.drawable.logout_outline))
                        .setTitle("User Logout")
                        .setMessage("Are you sure you want to logout from the app ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (firebaseUser != null){
                                    firebaseAuth.signOut();

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove(USERNAME);
                                    editor.remove(MOBILE_NUMBER);
                                    editor.remove(EMAIL_ID);
                                    editor.apply();

                                    cvLogout.setVisibility(View.GONE);
                                    viewPagerAdapter.replaceFragment(3, new LoginScreenFragment(), getResources().getString(R.string.account));
                                    viewPagerAdapter.notifyDataSetChanged();
                                }
                            }
                        }).setNegativeButton("No", null);
                builder.create().show();

            }
        });

    }


    @Override
    public void onBackPressed() {

        Fragment fragment = viewPagerAdapter.getItem(3);
        try{
            CardView cvBottomLogin = fragment.getView().findViewById(R.id.cvBottomLogin);
            BottomSheetBehavior<CardView> bottomSheetBehavior = BottomSheetBehavior.from(cvBottomLogin);

            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                super.onBackPressed();
            }

        }catch (Exception e){

        }

    }


    public void replaceFragment(int position, Fragment fragment, String title) {

        viewPagerAdapter.replaceFragment(position, fragment, title);
        viewPagerAdapter.notifyDataSetChanged();
    }
}
