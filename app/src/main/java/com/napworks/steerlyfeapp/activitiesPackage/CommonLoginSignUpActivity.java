package com.napworks.steerlyfeapp.activitiesPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.internal.service.Common;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.ConfirmAlertDialog;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonLoginSignUpActivity extends ParentActivity implements View.OnClickListener {
    public String TAG = getClass().getSimpleName();
    @BindView(R.id.edtMobileNumber)
    EditText edtMobileNumber;
    @BindView(R.id.getOtpButton)
    TextView getOtpButton;
    @BindView(R.id.countryCodeView)
    TextView countryCodeView;
    @BindView(R.id.byClickingText)
    TextView byClickingText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarTextView;

    private String type = "";
    ConfirmAlertDialog confirmAlertDialog;
    private String countryCode = "";
    String countryCallingCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup_activity);
        ButterKnife.bind(this);
        confirmAlertDialog = new ConfirmAlertDialog(this);


        if (getIntent() != null) {
            type = getIntent().getStringExtra(MyConstants.TYPE);
        }

        if (type.equalsIgnoreCase(MyConstants.SIGN_UP)) {
            CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.phoneVerification), toolbarTextView);
            byClickingText.setVisibility(View.GONE);
        } else {
            CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.login), toolbarTextView);
            byClickingText.setVisibility(View.VISIBLE);
            CommonMethods.setPrivacyTermsStyle(this, byClickingText.getText().toString().trim(), byClickingText);
        }

        DatabaseMethods databaseMethods = new DatabaseMethods(this);
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryId = manager.getSimCountryIso().toUpperCase();
        CommonMethods.showLog(TAG, "Id : " + countryId);
        if (countryId.equalsIgnoreCase("")) {
            String locale = getResources().getConfiguration().locale.getCountry();
            countryCallingCode = databaseMethods.getCountryCallingCode(locale);
            CommonMethods.showLog(TAG, "Country Calling Code : " + countryCallingCode);
            countryCodeView.setText(getString(R.string.plus).concat(countryCallingCode));
        } else {
            countryCallingCode = databaseMethods.getCountryCallingCode(countryId);
            CommonMethods.showLog(TAG, "Country Calling Code : " + countryCallingCode);
            countryCodeView.setText(getString(R.string.plus).concat(countryCallingCode));
        }


        getOtpButton.setOnClickListener(this);
        countryCodeView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getOtpButton:
                String mobileNumber = edtMobileNumber.getText().toString().trim();
//                String countryCodeText = countryCodeView.getText().toString().trim();

                if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {
                    edtMobileNumber.setError(getString(R.string.enter_valid_mobile_number));
                    edtMobileNumber.requestFocus();
                    return;
                } else {
//                    String finalMobileNumber = countryCodeText.concat(mobileNumber);
                    CommonMethods.showLog(TAG, "Number : " + mobileNumber + " Country Code : " + countryCallingCode);
                    confirmAlertDialog.showDialog(mobileNumber, countryCallingCode, type);
                }
                break;

            case R.id.countryCodeView:
                Intent intent = new Intent(this, CountryCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, MyConstants.SELECT_COUNTRY_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.SELECT_COUNTRY_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    countryCode = data.getStringExtra(MyConstants.COUNTRY_CALLING_CODE);
                    CommonMethods.showLog(TAG, "Code : " + countryCode);
                    countryCallingCode = countryCode;
                    countryCodeView.setText(getString(R.string.plus).concat(countryCode));
                }
            }
        }
    }

    //    @Override
//    public void onCountrySelected() {
//        countryCode = ccp.getSelectedCountryCodeWithPlus();
//        CommonMethods.showLog(TAG, "Country Code : " + countryCode);
//    }
}
