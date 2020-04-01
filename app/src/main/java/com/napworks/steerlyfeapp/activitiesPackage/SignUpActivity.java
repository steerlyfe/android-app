package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.LoginSignUpResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.LoginSignUpMainModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends ParentActivity implements View.OnClickListener, LoginSignUpResponseInterface {
    private String TAG = getClass().getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarTextView;
    @BindView(R.id.byClickingText)
    TextView byClickingText;
    @BindView(R.id.edtName)
    TextView edtName;
    @BindView(R.id.edtEmail)
    TextView edtEmail;
    @BindView(R.id.signUpButton)
    TextView signUpButton;

    LoadingDialog loadingDialog;
    CommonMessageDialog commonMessageDialog;
    SharedPreferences sharedPreferences;
    private String mobileNumber = "";
    private String countryCallingCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.signUp), toolbarTextView);
        loadingDialog = new LoadingDialog(this);
        commonMessageDialog = new CommonMessageDialog(this);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        CommonMethods.setPrivacyTermsStyle(this, byClickingText.getText().toString().trim(), byClickingText);

        if (getIntent() != null) {
            mobileNumber = getIntent().getStringExtra(MyConstants.MOBILE_NUMBER);
            countryCallingCode = getIntent().getStringExtra(MyConstants.COUNTRY_CALLING_CODE);
        }
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUpButton) {
            if (edtName.getText().toString().trim().isEmpty()) {
                commonMessageDialog.showDialog(getString(R.string.enterName));
            } else if (edtEmail.getText().toString().trim().isEmpty()) {
                commonMessageDialog.showDialog(getString(R.string.enterEmailAddress));
            } else {
                hitApi();
            }
        }
    }
    /*
     * SignUp Api
     */
    private void hitApi() {
        loadingDialog.showDialog();
        CommonWebServices.signUp(sharedPreferences,
                mobileNumber,
                edtName.getText().toString().trim(),
                edtEmail.getText().toString().trim(),
                countryCallingCode,
                this);
    }
    /*
     * Sign up Api Response
     */
    @Override
    public void getLoginSignUpResponse(String status, LoginSignUpMainModel loginSignUpMainModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (loginSignUpMainModel.getStatus() != null) {
                    switch (loginSignUpMainModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "Sign Up Api Success");
                            CommonMethods.showLog(TAG, "USER ID : " + loginSignUpMainModel.getUserDetail().getUserId());
                            CommonMethods.showLog(TAG, "Account ID : " + loginSignUpMainModel.getUserDetail().getAccountId());
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
                            finishAffinity();
                            Intent i = new Intent(SignUpActivity.this, QuizQuestionsActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(i);
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Sign Up Status 2 : " + loginSignUpMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "0":
                            commonMessageDialog.showDialog(loginSignUpMainModel.getMessage());
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
}
