package com.napworks.steerlyfeapp.dialogPackage;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.interfacePackage.ConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.CouponConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CouponListInnerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;


public class CouponConfirmationDialog implements View.OnClickListener {

    private final Dialog dialog;
    private final CouponConfirmationDialogResponseInterface couponConfirmationDialogResponseInterface;
    private Activity activity;
    private String TAG = getClass().getSimpleName();
    private TextView edit, ok, message;
    private String type = "";
    private CouponListInnerModel couponListInnerModel;

    public CouponConfirmationDialog(Activity activity, CouponConfirmationDialogResponseInterface couponConfirmationDialogResponseInterface) {

        this.activity = activity;
        this.couponConfirmationDialogResponseInterface = couponConfirmationDialogResponseInterface;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_coupon_confirmation);
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

    public void showDialog(String messageText, String type, CouponListInnerModel couponListInnerModel) {
        if (!dialog.isShowing()) {
            this.type = type;
            this.couponListInnerModel = couponListInnerModel;
            message.setText(messageText);
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
                couponConfirmationDialogResponseInterface.getCouponConfirmationDialogResponse(type,couponListInnerModel);

                break;
            case R.id.edit:
                hideDialog();
                break;
        }
    }
}
