package com.napworks.steerlyfeapp.designsPackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.interfacePackage.OnOptionSelectedInterface;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.modelPackage.ProductInfoModel;
import com.napworks.steerlyfeapp.modelPackage.SellerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomProductInfoContainer {

    private final Activity activity;

    private final LinearLayout inflatedProductInfoLayout;

    private List<ProductInfoModel> productInfoList;
    private String TAG = getClass().getSimpleName();

    public CustomProductInfoContainer(Activity activity, List<ProductInfoModel> productInfoList, LinearLayout inflatedProductInfoLayout) {
        this.activity = activity;
        this.productInfoList = productInfoList;
        this.inflatedProductInfoLayout = inflatedProductInfoLayout;
        setContainerDetail();
    }


    private void setContainerDetail() {
        for (int i = 0; i < productInfoList.size(); i++) {
            addProductInfoDataInContainer(i, productInfoList.get(i).getType(), productInfoList.get(i).getValue());
        }
    }




    private void addProductInfoDataInContainer(final int position, final String type, String value) {
        if (!type.isEmpty()) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mainView = inflater.inflate(R.layout.container_product_info, inflatedProductInfoLayout, false);
            TextView title = mainView.findViewById(R.id.title);
            TextView productValue = mainView.findViewById(R.id.productValue);
            title.setText(type);
            productValue.setText(value);
            CommonMethods.showLog(TAG, "Value : " + value + " at : " + position);
            inflatedProductInfoLayout.addView(mainView);
        }
    }
}
