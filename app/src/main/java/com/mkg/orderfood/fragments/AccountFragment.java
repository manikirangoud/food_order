package com.mkg.orderfood.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mkg.orderfood.Constants;
import com.mkg.orderfood.activities.CommonActivity;
import com.mkg.foodorder.R;
import com.mkg.orderfood.customviews.UbuntuTextView;
import com.mkg.orderfood.models.User;

import static com.mkg.orderfood.Constants.EMAIL_ID;
import static com.mkg.orderfood.Constants.MOBILE_NUMBER;
import static com.mkg.orderfood.Constants.SHARED_PREFS;
import static com.mkg.orderfood.Constants.USERNAME;

/**
 * Created by Manikiran Goud on 11-02-2018.
 */

public class AccountFragment extends Fragment {

    private View view;
    private ImageView ivArrow;
    private ImageView ivEdit;
    private TableRow tableRow1, tableRow2, tableRow3, tableRow4, tableRow5;

    private SharedPreferences sharedPreferences;
    private String username;
    private String mobileNumber;
    private String emailId;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;

    private UbuntuTextView utvUsername;
    private UbuntuTextView utvMobileNumber;
    private UbuntuTextView utvEmailId;

    private TableRow trMyAccount;
    private LinearLayout llMyAccountExtra;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.account_fragment_layout, container, false);


        ivArrow = view.findViewById(R.id.ivArrow);
        ivEdit = view.findViewById(R.id.ivEdit);
        trMyAccount = view.findViewById(R.id.trMyAccount);
        llMyAccountExtra = view.findViewById(R.id.llMyAccountExtra);
        tableRow1 = view.findViewById(R.id.table_row_1);
        tableRow2 = view.findViewById(R.id.table_row_2);
        tableRow3 = view.findViewById(R.id.table_row_3);
        tableRow4 = view.findViewById(R.id.table_row_4);
        tableRow5 = view.findViewById(R.id.table_row_5);

        //User details variables
        utvUsername = view.findViewById(R.id.utvUsername);
        utvMobileNumber = view.findViewById(R.id.utvMobileNumber);
        utvEmailId = view.findViewById(R.id.utvEmailId);


        //Fetching the shared prefs
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(USERNAME, "No username");
        mobileNumber = sharedPreferences.getString(MOBILE_NUMBER, "No mobile number");
        emailId = sharedPreferences.getString(EMAIL_ID, "No email id");



        //Setting the user details
        //setUserDetails();
        utvUsername.setText(username);
        utvMobileNumber.setText(mobileNumber);
        utvEmailId.setText(emailId);


      /*  trMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int height = llMyAccountExtra.getHeight();
                if(height == 0){
                    ivArrow.setImageResource(R.drawable.arrow_down);
                } else{
                    ivArrow.setImageResource(R.drawable.arrow_up);
                }
            }
        });*/


        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new EditUserDetailsBottomFragment();
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });


        tableRow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CommonActivity.class);
                startActivity(intent);
            }
        });

        tableRow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CommonActivity.class);
                startActivity(intent);
            }
        });

        tableRow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CommonActivity.class);
                startActivity(intent);
            }
        });

        tableRow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CommonActivity.class);
                startActivity(intent);
            }
        });

        tableRow5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CommonActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }


    //Setting the user details where ever needed
    private void setUserDetails(){

        //Getting user information from firebase
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users/" + Constants.USER_ID + "/" + "profileDetails");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                if(user != null){
                    utvUsername.setText(user.getUsername());
                    utvMobileNumber.setText(user.getMobileNumber());
                    utvEmailId.setText(user.getEmailId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
