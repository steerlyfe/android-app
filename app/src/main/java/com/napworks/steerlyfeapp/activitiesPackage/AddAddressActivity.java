package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.AddAddressResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CommonStringModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAddressActivity extends ParentActivity implements View.OnClickListener, AddAddressResponseInterface {

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.countryCodeView)
    TextView countryCodeView;
    @BindView(R.id.edtMobileNumber)
    EditText edtMobileNumber;
    @BindView(R.id.emailAddress)
    EditText emailAddress;
    @BindView(R.id.fullName)
    EditText fullName;
    @BindView(R.id.country)
    EditText country;
    @BindView(R.id.zipCode)
    EditText zipCode;
    @BindView(R.id.addressTextView)
    EditText addressTextView;
    @BindView(R.id.locality)
    EditText locality;
    @BindView(R.id.city)
    EditText city;
    @BindView(R.id.state)
    EditText state;
    @BindView(R.id.addButton)
    TextView addButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarText;

    String countryCallingCode = "";
    SharedPreferences sharedPreferences;
    LoadingDialog loadingDialog;
    CommonMessageDialog commonMessageDialog;
    DatabaseMethods databaseMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        commonMessageDialog = new CommonMessageDialog(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.addAddress), toolbarText);

        databaseMethods = new DatabaseMethods(this);
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryId = manager.getSimCountryIso().toUpperCase();
        CommonMethods.showLog(TAG, "Id : " + countryId);

        String countryName = "";

        if (countryId.equalsIgnoreCase("")) {
            String locale = getResources().getConfiguration().locale.getCountry();
            countryCallingCode = databaseMethods.getCountryCallingCode(locale);
            countryName = databaseMethods.getCountryName(locale);
            CommonMethods.showLog(TAG, "Country Calling Code : " + countryCallingCode);
            countryCodeView.setText(getString(R.string.plus).concat(countryCallingCode));
            country.setText(countryName);
        } else {
            countryCallingCode = databaseMethods.getCountryCallingCode(countryId);
            countryName = databaseMethods.getCountryName(countryId);
            CommonMethods.showLog(TAG, "Country Calling Code : " + countryCallingCode);
            countryCodeView.setText(getString(R.string.plus).concat(countryCallingCode));
            country.setText(countryName);
        }

        countryCodeView.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countryCodeView:
                Intent intent = new Intent(this, CountryCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, MyConstants.SELECT_COUNTRY_CODE);
                break;

            case R.id.addButton:
                if (!validateForm()) {
                    return;
                } else {

                    /*
                    * Add Address Api
                    */

                    loadingDialog.showDialog();
                    CommonWebServices.addAddress(sharedPreferences,
                            addressTextView.getText().toString().trim(),
                            zipCode.getText().toString().trim(),
                            city.getText().toString().trim(),
                            state.getText().toString().trim(),
                            countryCodeView.getText().toString().trim(),
                            edtMobileNumber.getText().toString().trim(),
                            country.getText().toString().trim(),
                            emailAddress.getText().toString().trim(),
                            fullName.getText().toString().trim(),
                            locality.getText().toString().trim(),
                            this);
                }
                break;
        }
    }


    /*
    *  This method is for validate form , all fields are validated or not
    */
    private boolean validateForm() {
        String mobileNumber = edtMobileNumber.getText().toString().trim();
        String emailText = emailAddress.getText().toString().trim();
        String fullNameText = fullName.getText().toString().trim();
        String countryText = country.getText().toString().trim();
        String zipCodeText = zipCode.getText().toString().trim();
        String addressText = addressTextView.getText().toString().trim();
        String cityText = city.getText().toString().trim();
        String stateText = state.getText().toString().trim();

        if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {
            commonMessageDialog.showDialog(getString(R.string.enter_valid_mobile_number));
            edtMobileNumber.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(emailText)) {
            commonMessageDialog.showDialog(getString(R.string.enterEmailAddress));
            emailAddress.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(fullNameText)) {
            commonMessageDialog.showDialog(getString(R.string.enterFullName));
            fullName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(countryText)) {
            commonMessageDialog.showDialog(getString(R.string.enterCountry));
            country.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(zipCodeText)) {
            commonMessageDialog.showDialog(getString(R.string.enterZipCode));
            zipCode.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(addressText)) {
            commonMessageDialog.showDialog(getString(R.string.enterAddress));
            addressTextView.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(cityText)) {
            commonMessageDialog.showDialog(getString(R.string.enterCityDistrict));
            city.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(stateText)) {
            commonMessageDialog.showDialog(getString(R.string.enterState));
            state.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.SELECT_COUNTRY_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    countryCallingCode = data.getStringExtra(MyConstants.COUNTRY_CALLING_CODE);
                    String countryName = data.getStringExtra(MyConstants.COUNTRY_NAME);
                    CommonMethods.showLog(TAG, "Code : " + countryCallingCode);
                    CommonMethods.showLog(TAG, "Country Name : " + countryName);
                    country.setText(countryName);
                    countryCodeView.setText(getString(R.string.plus).concat(countryCallingCode));

                }
            }
        }
    }

    /*
     * Add Address Api Response
     */
    @Override
    public void getAddAddressResponse(String status, CommonStringModel commonStringModel) {
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (commonStringModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Add Address Api Status : " + commonStringModel.getStatus());
                    switch (commonStringModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "Add Address Api Success");
                            setResult(RESULT_OK);
                            finish();
                            break;
                        case "2":
                            loadingDialog.hideDialog();
                            CommonMethods.showLog(TAG, "Add Address Status 2 : " + commonStringModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            loadingDialog.hideDialog();
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            loadingDialog.hideDialog();
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            loadingDialog.hideDialog();
                            commonMessageDialog.showDialog(commonStringModel.getMessage());
                            break;
                        default:
                            loadingDialog.hideDialog();
                            commonMessageDialog.showDialog(commonStringModel.getMessage());
                    }
                } else {
                    loadingDialog.hideDialog();
                    commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                }
                break;
            case MyConstants.NULL_RESPONSE:
            case MyConstants.FAILURE_RESPONSE:
                loadingDialog.hideDialog();
                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                break;
        }
    }
}
