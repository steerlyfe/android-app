package com.napworks.steerlyfeapp.dialogPackage;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.ProductsActivity;
import com.napworks.steerlyfeapp.designsPackage.CustomQuestionOptionsContainer;
import com.napworks.steerlyfeapp.designsPackage.CustomSortingContainer;
import com.napworks.steerlyfeapp.interfacePackage.CustomerHomeAdapterInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnFilterDialogInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnOptionSelectedInterface;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.modelPackage.QuizQuestionsModel;
import com.napworks.steerlyfeapp.modelPackage.SortingModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterCustomerDialog implements OnFilterDialogInterface, View.OnClickListener {
    private final Dialog dialog;

    @BindView(R.id.inflatedLayout)
    LinearLayout inflatedLayout;
    @BindView(R.id.crossImage)
    ImageView crossImage;
    private Activity activity;
    private List<SortingModel> sortingList;

    String TAG = getClass().getSimpleName();

    public FilterCustomerDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(this.activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_customer_dialog);
        View view = View.inflate(activity, R.layout.filter_customer_dialog, null);
        ButterKnife.bind(this, view);
        Window window = dialog.getWindow();
        if (window != null) {
            ButterKnife.bind(this, dialog);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);

            crossImage.setOnClickListener(this);
        }
    }

    public void showDialog(List<SortingModel> sortingList) {
        if (!dialog.isShowing()) {
            this.sortingList = sortingList;
            if (sortingList.size() > 0) {
                inflatedLayout.removeAllViews();
                new CustomSortingContainer(activity, sortingList, inflatedLayout, this);
            }
            dialog.show();
        }
    }

    private void hideDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void OnFilterDialogSelected(List<SortingModel> sortingList, int position) {
        if (activity instanceof ProductsActivity) {
            hideDialog();
            ((ProductsActivity) activity).updateSortingData(sortingList, position);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.crossImage) {
            hideDialog();
        }
    }
}
