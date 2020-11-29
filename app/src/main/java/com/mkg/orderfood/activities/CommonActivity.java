package com.mkg.orderfood.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mkg.foodorder.R;

/**
 * Created by Manikiran Goud on 17-02-2018.
 */

public class CommonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.common_layout);
    }


}
