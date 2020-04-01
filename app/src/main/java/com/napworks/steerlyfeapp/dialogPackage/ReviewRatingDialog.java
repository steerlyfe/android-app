package com.napworks.steerlyfeapp.dialogPackage;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.interfacePackage.ConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.RatingResponseInterface;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReviewRatingDialog implements View.OnClickListener {

    @BindView(R.id.rating)
    RatingBar ratingBar;
    @BindView(R.id.ratingTitle)
    TextView ratingTitle;
    @BindView(R.id.reviewText)
    TextView reviewText;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.done)
    TextView done;
    @BindView(R.id.comment)
    EditText comment;


    private final Dialog dialog;
    private final RatingResponseInterface ratingResponseInterface;
    private Activity activity;
    private String TAG = getClass().getSimpleName();
    private String type = "";

    public ReviewRatingDialog(Activity activity, RatingResponseInterface ratingResponseInterface) {

        this.activity = activity;
        this.ratingResponseInterface = ratingResponseInterface;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_review_rating);
        ButterKnife.bind(this, dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
            done.setOnClickListener(this);
            cancel.setOnClickListener(this);
        }
    }

    public void showDialog(String title, String type, String leftButtonText, String rightButtonText) {
        if (!dialog.isShowing()) {
            this.type = type;
            ratingTitle.setText(title);
            cancel.setText(leftButtonText);
            done.setText(rightButtonText);

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
            case R.id.done:
                CommonMethods.showLog(TAG, "Confirm");
                hideDialog();
                ratingResponseInterface.getRatingResponse(type, comment.getText().toString().trim(), ratingBar.getRating());

                break;
            case R.id.cancel:
                hideDialog();
                break;
        }
    }
}
