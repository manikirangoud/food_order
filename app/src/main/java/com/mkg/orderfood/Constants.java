package com.mkg.orderfood;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mkg.orderfood.models.User;

public class Constants {


    public static final String ZOMATO_API_KEY = "faffab740c3ab301d5cc09c45e167b83";
    public static final int RESULT_MAPS = 1;
    public static final int RESULT_OTP = 2;
    public static final int RESULT_FILTERS = 3;
    public static final String SHARED_PREFS = "User_prefs";

    //Shared prefs names
    public static final String USERNAME = "username";
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String EMAIL_ID = "emailId";
    public static double CURRENT_LAT = 17.5017935;
    public static double CURRENT_LONG = 78.4176572;


    public static String USER_ID = "";
    public static User user;


    public static User GetUser(String userId){

        if(userId.trim().length() == 0){
            userId = GetUserId();
        }
        //Getting user information from firebase
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users/" + userId + "/" + "profileDetails");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if(user != null){
                    Constants.user = user;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Constants.user;

    }


    public static String GetUserId(){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Constants.USER_ID = firebaseUser.getUid();
            return Constants.USER_ID;
        }
        return "";
    }


}



