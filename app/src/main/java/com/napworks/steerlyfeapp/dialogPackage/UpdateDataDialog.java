package com.napworks.steerlyfeapp.dialogPackage;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.interfacePackage.UpdateProfileDialogInterFace;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateDataDialog implements View.OnClickListener {
    private final Dialog dialog;
    private final UpdateProfileDialogInterFace updateProfileDialogInterFace;


    @BindView(R.id.emailAddress)
    EditText emailAddress;
    @BindView(R.id.updateButton)
    TextView updateButton;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.cancelButton)
    TextView cancelButton;
    Activity activity;
    private String type = "";
    private SharedPreferences sharedPreferences;


    public UpdateDataDialog(Activity activity, UpdateProfileDialogInterFace updateProfileDialogInterFace) {
        this.activity = activity;
        this.updateProfileDialogInterFace = updateProfileDialogInterFace;

        dialog = new Dialog(this.activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_data);
        ButterKnife.bind(this, dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            ButterKnife.bind(this, dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);


            updateButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);

        }
    }

    public void showDialog(SharedPreferences sharedPreferences) {
        if (!dialog.isShowing()) {
            this.sharedPreferences = sharedPreferences;
            name.setText(sharedPreferences.getString(MyConstants.FULL_NAME, ""));
            emailAddress.setText(sharedPreferences.getString(MyConstants.EMAIL, ""));
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
            case R.id.updateButton:
                if (name.getText().toString().trim().isEmpty()) {
                    name.setError(activity.getString(R.string.enterName));
                } else if (emailAddress.getText().toString().trim().isEmpty()) {
                    emailAddress.setError(activity.getString(R.string.enterEmailAddress));
                } else {
                    hideDialog();
                    updateProfileDialogInterFace.updateProfileDialogInterFace(name.getText().toString().trim(), emailAddress.getText().toString().trim());
                }
                break;

            case R.id.cancelButton:
                hideDialog();
                break;

        }
    }
}
