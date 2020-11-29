package com.mkg.orderfood.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.mkg.foodorder.R;
import com.mkg.orderfood.activities.SignUpActivity;
import com.mkg.orderfood.customviews.UbuntuTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mkg.orderfood.models.User;

import static com.mkg.orderfood.Constants.RESULT_OTP;

/**
 * Created by Manikiran Goud on 13-02-2018.
 */

public class LoginBottomSheetFragment extends BottomSheetDialogFragment {

    private UbuntuTextView utvSendOtp;
    private EditText etMobileNumber;
    private CountryCodePicker countryCodePicker;


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

        final View view = View.inflate(getContext(), R.layout.login_layout,null);
        dialog.setContentView(view);


        utvSendOtp = view.findViewById(R.id.utvSendOtp);
        etMobileNumber = view.findViewById(R.id.et_mobile_number);
        countryCodePicker = view.findViewById(R.id.ccp_country_code);


        utvSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String countryCode = countryCodePicker.getSelectedCountryCode();
                final String number = etMobileNumber.getText().toString();

                final String fullNumber = countryCode + "-" + number;

                if( number.isEmpty() || number.length() < 10) {
                    Toast.makeText(getActivity(), "Please provide a valid phone number", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference().child("users");

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.hasChild(fullNumber)) {
                                    //User is already registered we have to just ask the otp to confirm
                                } else {

                                    //User is not registered we have to ask basic details
                                    Intent intentSignUp = new Intent(getActivity(), SignUpActivity.class);
                                    intentSignUp.putExtra("phoneNumber", number);
                                    intentSignUp.putExtra("countryCode", countryCode);
                                    startActivityForResult(intentSignUp, RESULT_OTP);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }catch (Exception e){
                        Log.e("ERR_OTP", e.toString());
                    }

                }
            }
        });

        CoordinatorLayout.LayoutParams params = ( CoordinatorLayout.LayoutParams ) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if ( behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main, new AccountFragment());
        fragmentTransaction.commit();
        dismiss();*/

        if(resultCode == RESULT_OTP){

            try {
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.account));
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
                Log.e("Error- reload frg", "Refreshed fragment");
            }catch (Exception e){
                Log.e("Error- reload frg", e.toString());
            }
        }



    }
}
