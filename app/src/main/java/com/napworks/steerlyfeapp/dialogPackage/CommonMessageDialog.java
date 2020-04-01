package com.napworks.steerlyfeapp.dialogPackage;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonMessageDialog implements View.OnClickListener {
    private final Dialog dialog;


    @BindView(R.id.okText)
    TextView okText;
    @BindView(R.id.message)
    TextView messageText;

    Activity activity;


    public CommonMessageDialog(Activity activity) {
        this.activity = activity;

        dialog = new Dialog(this.activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_common_message);
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

            okText.setOnClickListener(this);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

    public void showDialog(String message) {
        if (!dialog.isShowing()) {
            messageText.setText(message);
            dialog.show();
        }
    }

    private void hideDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okText) {
            hideDialog();
        }
    }
}
