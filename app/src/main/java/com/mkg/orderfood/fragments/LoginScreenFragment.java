package com.mkg.orderfood.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.mkg.foodorder.R;
import com.mkg.orderfood.MainActivity;
import com.mkg.orderfood.customviews.UbuntuTextView;
import com.mkg.orderfood.models.User;

import java.util.concurrent.TimeUnit;

/**
 * Created by Manikiran Goud on 13-02-2018.
 */

public class LoginScreenFragment extends Fragment {

    private View view;
    private LinearLayout llAccountLogin;
    private UbuntuTextView utvLogin;
    private UbuntuTextView utvSendOtp;
    private CardView cvBottomLogin;
    private BottomSheetBehavior<CardView> bottomSheetBehavior;
    private LinearLayout llDetails;
    private LinearLayout llSignUp;
    private ProgressBar pbLogin;
    private TableRow trOtpVerification;
    private TableRow trMobileNumber;


    //Variables of login and sign up layout
    private String countryCode;
    private String mobileNumber;
    private String emailId;
    private String username;
    private TextInputLayout tilReferralCode;
    private CountryCodePicker countryCodePicker;
    private EditText etMobileNumber;
    private EditText etEmailId;
    private EditText etUserName;
    private EditText etReferralCode;
    private EditText etOtp;
    private CheckBox checkBoxReferral;
    private UbuntuTextView utvSignUp;
    private UbuntuTextView utvVerifyOtp;

    private String verificationCode;
    private String otpCode;
    private FirebaseAuth firebaseAuth;
    private boolean isMobileVerified = false;
    private boolean verificationInProgress = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.login_screen_layout, container, false);


        //Binding views of login layout
        utvLogin = view.findViewById(R.id.utvLogin);
        utvSendOtp = view.findViewById(R.id.utvSendOtp);
        llAccountLogin = view.findViewById(R.id.llAccountLogin);
        cvBottomLogin = view.findViewById(R.id.cvBottomLogin);
        bottomSheetBehavior = BottomSheetBehavior.from(cvBottomLogin);
        utvVerifyOtp = view.findViewById(R.id.utvVerifyOtp);
        etOtp = view.findViewById(R.id.etOtp);
        etMobileNumber = view.findViewById(R.id.et_mobile_number);
        countryCodePicker = view.findViewById(R.id.ccp_country_code);
        llDetails = view.findViewById(R.id.llDetails);
        llSignUp = view.findViewById(R.id.llSignUp);
        pbLogin = view.findViewById(R.id.pbLogin);
        trOtpVerification = view.findViewById(R.id.trOtpVerification);
        trMobileNumber = view.findViewById(R.id.trMobileNumber);




        //Binding views of sign up layout
        etEmailId = view.findViewById(R.id.et_email_sign_up);
        etUserName = view.findViewById(R.id.et_name);
        utvSignUp = view.findViewById(R.id.utvSignUp);
        checkBoxReferral = view.findViewById(R.id.check_box_referral);
        etReferralCode = view.findViewById(R.id.et_referral_code);
        tilReferralCode = view.findViewById(R.id.til_referral_code);



        //Initializing local variables
        firebaseAuth = FirebaseAuth.getInstance();


        //Expanding the bottom sheet for login / sign up
        utvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });


        //Bottom sheet states
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    llAccountLogin.setAlpha(0.3f);
                } else if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    llAccountLogin.setAlpha(1.0f);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        try {

            //After validating the details sending otp
            utvSendOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    countryCode =  countryCodePicker.getSelectedCountryCode();
                    mobileNumber = etMobileNumber.getText().toString();

                    if (countryCode.length() == 0) {
                        Toast.makeText(getActivity(), "Please select the country code", Toast.LENGTH_LONG).show();
                    } else if (mobileNumber.length() < 10){
                        Toast.makeText(getActivity(), "Please provide a valid mobile number", Toast.LENGTH_LONG).show();
                    } else{
                        if(!verificationInProgress){
                            verificationInProgress = true;
                            llDetails.setAlpha(0.3f);
                            pbLogin.setVisibility(View.VISIBLE);
                            sentOtp();
                        } else {
                            Toast.makeText(getActivity(), "Please wait, already a verification is in progress..", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


        }catch (Exception e){
            Log.e("ERR_OTP", e.toString());
        }



        //After entering the otp just verifying it
        utvVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpCode = etOtp.getText().toString();
                if (otpCode.length() < 6) {
                    Toast.makeText(getActivity(), "Please enter a 6 digit verification code!", Toast.LENGTH_LONG).show();
                } else {
                    llDetails.setAlpha(0.3f);
                    pbLogin.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otpCode);
                    signInWithPhone(credential);
                }
            }
        });



        //For toggling the referral code
        checkBoxReferral.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tilReferralCode.setVisibility(View.VISIBLE);
                } else {
                    tilReferralCode.setVisibility(View.GONE);
                }
            }
        });


        //On Sign up clicked
        utvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUserName.getText().toString().trim();
                emailId = etEmailId.getText().toString().trim();

                if(username.length() == 0){
                    Toast.makeText(getActivity(), "Please provide a name", Toast.LENGTH_LONG).show();
                } else if(emailId.length() == 0){
                    Toast.makeText(getActivity(), "Please provide an email id", Toast.LENGTH_LONG).show();
                } else {

                    //Storing the details
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser != null){
                        createUserDetails(firebaseAuth.getUid(), username, emailId);
                    }
                }
            }
        });


        return view;
    }


    private void createUserDetails(final String userId,final String username,final String emailId) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference usersReference = database.getReference().child("users");

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DatabaseReference dbRef = usersReference.child(userId);

                User user = new User(countryCode, mobileNumber, username, emailId);

                dbRef.child("profileDetails")
                    .setValue(user, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error != null) {
                                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                etEmailId.setText("");
                                etMobileNumber.setText("");
                                llSignUp.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "You have successfully registered", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Sending the otp for mobile number
    private void sentOtp() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+" + countryCode + mobileNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                verificationInProgress = false;
                                Toast.makeText(getActivity(), "onVerificationCompleted - Automatically", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                verificationInProgress = false;
                                llDetails.setAlpha(1.0f);
                                pbLogin.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Unable to send Otp, please try again", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                //Setting the layout opacity, progress
                                llDetails.setAlpha(1.0f);
                                pbLogin.setVisibility(View.GONE);

                                //Setting the Mobile Number row disabled
                                trMobileNumber.setEnabled(false);
                                countryCodePicker.setClickable(false);
                                countryCodePicker.setFocusable(false);
                                etMobileNumber.setEnabled(false);

                                //Displaying the Otp Verification row
                                trOtpVerification.setVisibility(View.VISIBLE);

                                //Setting the visibility gone to the send otp utv text
                                utvSendOtp.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Verification code has been sent", Toast.LENGTH_LONG).show();
                            }


                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    //Checking the opt and verification code
    private void signInWithPhone(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Successfully verified", Toast.LENGTH_SHORT).show();

                            verificationInProgress = false;

                            llDetails.setAlpha(1.0f);
                            pbLogin.setVisibility(View.GONE);
                            trOtpVerification.setVisibility(View.GONE);
                            //Setting the Mobile Number row disabled
                            trMobileNumber.setEnabled(false);

                            //Getting the user details
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if(firebaseUser != null){
                                checkUserRegistered(firebaseUser.getUid());
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please enter correct otp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //Checking for the user
    private void checkUserRegistered(String userId) {

        //Getting user information from firebase
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users/" + userId + "/" + "profileDetails");
        final boolean[] isUserRegis = {false};

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);

                if(user == null){
                    //Asking user to give the extra details
                    llSignUp.setVisibility(View.VISIBLE);
                    utvSignUp.setVisibility(View.VISIBLE);
                } else{
                    if (getActivity() != null){
                        MainActivity mainActivity =  (MainActivity)getActivity();
                        mainActivity.replaceFragment(3, new LoginScreenFragment(), getResources().getString(R.string.account));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
