package com.napworks.steerlyfeapp.dialogPackage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.VerifyMobileNumberActivity;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;


public class ConfirmAlertDialog implements View.OnClickListener {

    private final Dialog dialog;
    private Activity activity;
    private String TAG = getClass().getSimpleName();
    private TextView edit, ok, message_center_text;
    private String type = "";
    private String countryCallingCode = "";
    private String mobileNumber = "";

    public ConfirmAlertDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_alert);
        Window window = dialog.getWindow();
        if (window != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
            ok = dialog.findViewById(R.id.ok);
            edit = dialog.findViewById(R.id.edit);
            message_center_text = dialog.findViewById(R.id.message_center_text);
            ok.setOnClickListener(this);
            edit.setOnClickListener(this);
        }
    }

    public void showDialog(String mobileNumber, String countryCallingCode, String type) {

        if (!dialog.isShowing()) {
            this.mobileNumber = mobileNumber;
            this.countryCallingCode = countryCallingCode;
            this.type = type;
            message_center_text.setText(activity.getString(R.string.plus).concat(countryCallingCode).concat(mobileNumber));
            dialog.show();
        }
    }

    public void hideDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                CommonMethods.showLog(TAG, "Confirm : " + message_center_text.getText().toString().trim());
                hideDialog();
                Intent intent = new Intent(activity, VerifyMobileNumberActivity.class);
                intent.putExtra(MyConstants.MOBILE_NUMBER, mobileNumber);
                intent.putExtra(MyConstants.COUNTRY_CALLING_CODE, countryCallingCode);
                intent.putExtra(MyConstants.TYPE, type);
                activity.startActivity(intent);
                break;
            case R.id.edit:
                hideDialog();
//                dialog.dismiss();
                break;
        }
    }
}
