package com.mkg.orderfood.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;

import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mkg.foodorder.R;

/**
 * Created by Manikiran Goud on 17-02-2018.
 */

public class EditUserDetailsBottomFragment extends BottomSheetDialogFragment {


    private EditText editTextNumber, editTextEmailId;
    private Button buttonUpdate;


    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            if(newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        final View view = View.inflate(getContext(), R.layout.update_user_details_layout,null);
        dialog.setContentView(view);


        buttonUpdate = ( Button ) view.findViewById(R.id.button_update);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        CoordinatorLayout.LayoutParams params = ( CoordinatorLayout.LayoutParams ) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if ( behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
        }

    }



}
