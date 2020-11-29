package com.mkg.orderfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mkg.orderfood.MainActivity;
import com.mkg.foodorder.R;

/**
 * Created by Manikiran Goud on 23-02-2018.
 */

public class SplashActivity extends AppCompatActivity {

    private ImageView imageViewChicken, imageViewBurger, imageViewDrink, imageViewPizza, imageViewDessert;
    private Animation zoomIn1, zoomIn2, zoomIn3, zoomIn4, zoomIn5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_activity_layout);

        imageViewBurger = findViewById(R.id.burger);
        imageViewChicken = findViewById(R.id.chicken);
        imageViewDessert = findViewById(R.id.dessert);
        imageViewDrink = findViewById(R.id.drink);
        imageViewPizza = findViewById(R.id.pizza);


        zoomIn1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        zoomIn2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        zoomIn3 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        zoomIn4 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        zoomIn5 = AnimationUtils.loadAnimation(this, R.anim.fade_in);


        imageViewPizza.startAnimation(zoomIn1);

        zoomIn1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageViewPizza.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewBurger.startAnimation(zoomIn2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        zoomIn2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageViewBurger.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewChicken.startAnimation(zoomIn3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        zoomIn3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageViewChicken.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewDessert.startAnimation(zoomIn4);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        zoomIn4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageViewDessert.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewDrink.startAnimation(zoomIn5);
                imageViewDrink.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        },2000);
    }
}
