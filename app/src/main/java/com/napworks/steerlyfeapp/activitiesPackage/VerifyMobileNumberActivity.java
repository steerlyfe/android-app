package com.napworks.steerlyfeapp.activitiesPackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.LoginSignUpResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.LoginSignUpMainModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyMobileNumberActivity extends ParentActivity implements View.OnClickListener, LoginSignUpResponseInterface, TextWatcher {
    public String TAG = getClass().getSimpleName();

    @BindView(R.id.first)
    EditText first;
    @BindView(R.id.second)
    EditText second;
    @BindView(R.id.third)
    EditText third;
    @BindView(R.id.fourth)
    EditText fourth;
    @BindView(R.id.fifth)
    EditText fifth;
    @BindView(R.id.sixth)
    EditText sixth;
    @BindView(R.id.verifyText)
    TextView verifyText;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarTextView;
    @BindView(R.id.resendCode)
    TextView resendCode;

    private String mVerificationId = "", code = "";
    private FirebaseAuth mAuth;
    LoadingDialog loadingDialog;
    String mobileNumber = "", finalCode = "";
    SharedPreferences sharedPreferences;
    CommonMessageDialog commonMessageDialog;
    private String type = "";
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String countryCallingCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile_number);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.verifyOtp), toolbarTextView);
        commonMessageDialog = new CommonMessageDialog(this);
        loadingDialog = new LoadingDialog(this);
        mainLinear.setVisibility(View.GONE);
        loadingDialog.showDialog();
        if (getIntent() != null) {
            mobileNumber = getIntent().getStringExtra(MyConstants.MOBILE_NUMBER);
            countryCallingCode = getIntent().getStringExtra(MyConstants.COUNTRY_CALLING_CODE);
            type = getIntent().getStringExtra(MyConstants.TYPE);
            CommonMethods.showLog(TAG, "Mobile Number : " + mobileNumber);
            sendVerificationCode(getString(R.string.plus).concat(countryCallingCode).concat(mobileNumber));
        }
        verifyText.setOnClickListener(this);
        resendCode.setOnClickListener(this);
        first.addTextChangedListener(this);
        second.addTextChangedListener(this);
        third.addTextChangedListener(this);
        fourth.addTextChangedListener(this);
        fifth.addTextChangedListener(this);
        sixth.addTextChangedListener(this);
    }
    /*
     * Send Verification Code to mobile
     */

    private void sendVerificationCode(String mobileNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                loadingDialog.showDialog();

                if (code.length() == 6) {
                    first.setText(code.substring(0, 1));
                    second.setText(code.substring(1, 2));
                    third.setText(code.substring(2, 3));
                    fourth.setText(code.substring(3, 4));
                    fifth.setText(code.substring(4, 5));
                    sixth.setText(code.substring(5, 6));
                }
                CommonMethods.showLog(TAG, "Code is : " + code);
                verifyVerificationCode(code);
            } else {
                CommonMethods.showLog(TAG, "Code is Null");
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            loadingDialog.hideDialog();
            CommonMethods.showLog(TAG, "Verification Failed : " + e.getMessage());
            Toast.makeText(VerifyMobileNumberActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            loadingDialog.hideDialog();
            mainLinear.setVisibility(View.VISIBLE);
            mVerificationId = s;
            resendToken = forceResendingToken;

        }
    };

    /*
     * Verify  Verification Code to mobile
     */
    private void verifyVerificationCode(String code) {
        finalCode = code;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        CommonMethods.showLog(TAG, "Credential Provider : " + credential.getProvider());
        CommonMethods.showLog(TAG, "Credential SignInMethod : " + credential.getSignInMethod());
        CommonMethods.showLog(TAG, "Credential SmsCode : " + credential.getSmsCode());
        CommonMethods.showLog(TAG, "Verification Id : " + mVerificationId);
        signInWithPhoneAuthCredential(credential);
    }
    /*
     * Sign In
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingDialog.showDialog();
                            CommonMethods.showLog(TAG, "Verified " + mVerificationId);
                            CommonWebServices.login(mobileNumber,
                                    sharedPreferences.getString(MyConstants.NOTIFICATION_TOKEN, ""),
                                    countryCallingCode,
                                    VerifyMobileNumberActivity.this
                            );
                        } else {
                            loadingDialog.hideDialog();
                            String message;
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered";
                                Toast.makeText(VerifyMobileNumberActivity.this, message, Toast.LENGTH_SHORT).show();
                                CommonMethods.showLog(TAG, "Message : " + message);
                                CommonMethods.showLog(TAG, "Exception Message : " + task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verifyText:
                String enteredCode = first.getText().toString().trim().
                        concat(second.getText().toString().trim()).
                        concat(third.getText().toString().trim()).
                        concat(fourth.getText().toString().trim()).
                        concat(fifth.getText().toString().trim()).
                        concat(sixth.getText().toString().trim());
                CommonMethods.showLog(TAG, "Code : " + code);
                if (enteredCode.length() != 6) {
                    commonMessageDialog.showDialog(getString(R.string.enter_valid_verification_code));
                } else {
                    loadingDialog.showDialog();
                    verifyVerificationCode(enteredCode);
                }
                break;

            case R.id.resendCode:
                loadingDialog.showDialog();
                resendVerificationCode(getString(R.string.plus).concat(countryCallingCode).concat(mobileNumber), resendToken);
                break;
        }
    }
    /*
     * Resend Verification Code to mobile
     */
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    /*
     * Login Api Response
     */
    @Override
    public void getLoginSignUpResponse(String status, LoginSignUpMainModel loginSignUpMainModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (loginSignUpMainModel.getStatus() != null) {
                    switch (loginSignUpMainModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "Login Api Success");
                            CommonMethods.showLog(TAG, "USER ID : " + loginSignUpMainModel.getUserDetail().getUserId());
                            CommonMethods.showLog(TAG, "Auth : " + loginSignUpMainModel.getUserDetail().getAuthToken());
                            CommonMethods.showLog(TAG, "Session : " + loginSignUpMainModel.getUserDetail().getSessionToken());
                            CommonMethods.showLog(TAG, "Mob number : " +loginSignUpMainModel.getUserDetail().getPhoneNumber());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(MyConstants.USER_ID, loginSignUpMainModel.getUserDetail().getUserId());
                            editor.putString(MyConstants.SESSION, loginSignUpMainModel.getUserDetail().getSessionToken());
                            editor.putString(MyConstants.FULL_NAME, loginSignUpMainModel.getUserDetail().getFullName());
                            editor.putString(MyConstants.PHONE_NUMBER, loginSignUpMainModel.getUserDetail().getPhoneNumber());
                            editor.putString(MyConstants.EMAIL, loginSignUpMainModel.getUserDetail().getEmail());
                            editor.putString(MyConstants.AUTHORIZATION, loginSignUpMainModel.getUserDetail().getAuthToken());
                            editor.putString(MyConstants.ACCOUNT_ID, loginSignUpMainModel.getUserDetail().getAccountId());
                            editor.putInt(MyConstants.QUESTION_SUBMITTED, loginSignUpMainModel.getUserDetail().getQuestionSubmitted());
                            editor.apply();

                            if (loginSignUpMainModel.getUserDetail().getQuestionSubmitted() == 1 ||
                                    loginSignUpMainModel.getUserDetail().getQuestionSubmitted() == 2) {
                                Intent i = new Intent(VerifyMobileNumberActivity.this, MainActivity.class);
                                i.putExtra(MyConstants.CALLED_FROM, "");
                                startActivity(i);
                                finishAffinity();
                                overridePendingTransition(0, 0);
                            } else {
                                Intent i = new Intent(VerifyMobileNumberActivity.this, QuizQuestionsActivity.class);
                                startActivity(i);
                                finishAffinity();
                                overridePendingTransition(0, 0);
                            }


                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Login Status 2 : " + loginSignUpMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "0":
                            if (type.equalsIgnoreCase(MyConstants.LOGIN)) {
                                Toast.makeText(this, getString(R.string.you_dont_have_account_text), Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(VerifyMobileNumberActivity.this, SignUpActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra(MyConstants.MOBILE_NUMBER, mobileNumber);
                            intent.putExtra(MyConstants.COUNTRY_CALLING_CODE, countryCallingCode);
                            overridePendingTransition(0, 0);
                            startActivity(intent);
                            break;
                    }
                } else {
                    commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                }
                break;
            case MyConstants.NULL_RESPONSE:
            case MyConstants.FAILURE_RESPONSE:
                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        CommonMethods.showLog(TAG, "beforeTextChanged");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        CommonMethods.showLog(TAG, "OnTextChanged");
    }

    @Override
    public void afterTextChanged(Editable s) {

        CommonMethods.showLog(TAG, "afterTextChanged");

        if (first.getText().hashCode() == s.hashCode()) {
            if (s.length() == 1) {
                second.requestFocus();
            }
        } else if (second.getText().hashCode() == s.hashCode()) {
            if (s.length() == 1) {
                third.requestFocus();
            } else {
                first.requestFocus();
            }
        } else if (third.getText().hashCode() == s.hashCode()) {
            if (s.length() == 1) {
                fourth.requestFocus();
            } else {
                second.requestFocus();
            }
        } else if (fourth.getText().hashCode() == s.hashCode()) {
            if (s.length() == 1) {
                fifth.requestFocus();
            } else {
                third.requestFocus();
            }
        } else if (fifth.getText().hashCode() == s.hashCode()) {
            if (s.length() == 1) {
                sixth.requestFocus();
            } else {
                fourth.requestFocus();
            }
        } else if (sixth.getText().hashCode() == s.hashCode()) {
            if (s.length() == 0) {
                fifth.requestFocus();
            }
        }

    }
}
