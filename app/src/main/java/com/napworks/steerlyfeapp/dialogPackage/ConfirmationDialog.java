package com.napworks.steerlyfeapp.dialogPackage;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.interfacePackage.ConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;


public class ConfirmationDialog implements View.OnClickListener {

    private final Dialog dialog;
    private final ConfirmationDialogResponseInterface confirmationDialogResponseInterface;
    private Activity activity;
    private String TAG = getClass().getSimpleName();
    private TextView edit, ok, message;
    private String type = "";

    public ConfirmationDialog(Activity activity, ConfirmationDialogResponseInterface confirmationDialogResponseInterface) {

        this.activity = activity;
        this.confirmationDialogResponseInterface = confirmationDialogResponseInterface;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirmation);
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
            message = dialog.findViewById(R.id.message);
            ok.setOnClickListener(this);
            edit.setOnClickListener(this);
        }
    }

    public void showDialog(String messageText, String type, String leftButtonText, String rightButtonText) {
        if (!dialog.isShowing()) {
            this.type = type;
            message.setText(messageText);
            edit.setText(leftButtonText);
            ok.setText(rightButtonText);
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
                CommonMethods.showLog(TAG, "Confirm");
                hideDialog();
                confirmationDialogResponseInterface.getConfirmationDialogResponse(type);

                break;
            case R.id.edit:
                hideDialog();
                if (type.equalsIgnoreCase(MyConstants.DEFAULT_ADDRESS)) {
                    confirmationDialogResponseInterface.getConfirmationDialogResponse(MyConstants.OPEN_ADDRESS_ACTIVITY);
                }
                break;
        }
    }
}
