package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.CommonStringResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CommonStringModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateDataActivity extends AppCompatActivity implements View.OnClickListener, CommonStringResponseInterface {

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.emailAddress)
    EditText emailAddress;
    @BindView(R.id.updateButton)
    TextView updateButton;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarText;
    private SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    private CommonMessageDialog commonMessageDialog;
    private String nameText = "", emailText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        ButterKnife.bind(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.accountInfo), toolbarText);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        commonMessageDialog = new CommonMessageDialog(this);

        name.setText(sharedPreferences.getString(MyConstants.FULL_NAME, ""));
        emailAddress.setText(sharedPreferences.getString(MyConstants.EMAIL, ""));

        updateButton.setOnClickListener(this);

    }
    /*
     * Update Profile Api
     */
    private void hitApi(String name, String email) {
        CommonMethods.showLog(TAG, "name : " + name);
        CommonMethods.showLog(TAG, "email : " + email);
        CommonWebServices.updateProfileInfo(sharedPreferences,
                name,
                email,
                this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.updateButton) {
            if (name.getText().toString().trim().isEmpty()) {
                name.setError(getString(R.string.enterName));
            } else if (emailAddress.getText().toString().trim().isEmpty()) {
                emailAddress.setError(getString(R.string.enterEmailAddress));
            } else {
                loadingDialog.showDialog();
                nameText = name.getText().toString().trim();
                emailText = emailAddress.getText().toString().trim();
                hitApi(nameText, emailText);
            }
        }
    }
    /*
     * Update Profile Api Response
     */
    @Override
    public void getCommonStringResponse(String status, CommonStringModel commonStringModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (commonStringModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Update Data Api Status : " + commonStringModel.getStatus());
                    switch (commonStringModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "Update Data Api Success");
//                            commonMessageDialog.showDialog(commonStringModel.getMessage());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(MyConstants.FULL_NAME, nameText);
                            editor.putString(MyConstants.EMAIL, emailText);
                            editor.apply();
                            name.setText(nameText);
                            emailAddress.setText(emailText);

                            Intent intent=new Intent();
                            intent.putExtra(MyConstants.MESSAGE,commonStringModel.getMessage());
                            setResult(RESULT_OK,intent);
                            finish();
                            overridePendingTransition(0, 0);

                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Update Data Status 2 : " + commonStringModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "0":
                            commonMessageDialog.showDialog(commonStringModel.getMessage());
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
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
