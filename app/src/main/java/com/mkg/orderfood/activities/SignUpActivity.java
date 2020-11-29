package com.mkg.orderfood.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.mkg.foodorder.R;
import com.mkg.orderfood.customviews.UbuntuTextView;

import java.util.concurrent.TimeUnit;

import static com.mkg.orderfood.Constants.RESULT_OTP;

/**
 * Created by Manikiran Goud on 13-02-2018.
 */

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Bundle bundle;
    private String countryCode;
    private String phoneNumber;
    private String emailId;
    private String username;
    private TextInputLayout tilReferralCode;
    private CountryCodePicker countryCodePicker;
    private EditText etPhoneNumber;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_layout);

        toolbar = findViewById(R.id.toolbar_sign_up);
        etPhoneNumber = findViewById(R.id.et_mobile_number);
        etEmailId = findViewById(R.id.et_email_sign_up);
        etUserName = findViewById(R.id.et_name);
        utvSignUp = findViewById(R.id.utvSignUp);
        checkBoxReferral = findViewById(R.id.check_box_referral);
        etReferralCode = findViewById(R.id.et_referral_code);
        tilReferralCode = findViewById(R.id.til_referral_code);
        countryCodePicker = findViewById(R.id.ccp_country_code);
        utvVerifyOtp = findViewById(R.id.utvVerifyOtp);
        etOtp = findViewById(R.id.etOtp);


        bundle = getIntent().getExtras();
        countryCode = bundle.getString("countryCode", "");
        phoneNumber = bundle.getString("phoneNumber", "");

        try {

            //Setting the details of mobile number and country code
            if (countryCode.length() > 0) {
                countryCodePicker.setCountryForPhoneCode(Integer.parseInt(countryCode));
            }
            etPhoneNumber.setText(phoneNumber);

            //Sending the otp
            if (countryCode.length() > 0 && phoneNumber.length() > 0) {

                firebaseAuth = FirebaseAuth.getInstance();
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(firebaseAuth)
                                .setPhoneNumber("+" + countryCode + phoneNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // Activity (for callback binding)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(s, forceResendingToken);
                                        verificationCode = s;
                                        Toast.makeText(getApplicationContext(), "Verification code has been sent", Toast.LENGTH_LONG).show();
                                    }
                                })          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

            //After entering the otp just verifying it
            utvVerifyOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    otpCode = etOtp.getText().toString();
                    if (otpCode.length() < 4) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid otp !", Toast.LENGTH_LONG).show();
                    } else {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otpCode);
                        signInWithPhone(credential);
                    }
                }
            });


            //When the sign up button clicked by user
            utvSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    emailId = etEmailId.getText().toString();
                    username = etEmailId.getText().toString();
                    setResult(RESULT_OTP);
                     finish();
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

        }catch (Exception e){
            Log.e("ERR_OTP", e.toString());
        }

    }


    //Checking the opt and verification code
    private void signInWithPhone(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Succesfully verified", Toast.LENGTH_SHORT).show();
                            isMobileVerified = true;
                        } else {
                            Toast.makeText(SignUpActivity.this, "Please enter correct otp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
