package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.fragmentsPackage.SellerFragment;
import com.napworks.steerlyfeapp.modelPackage.ProductAvailabilityModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.SellerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.io.Serializable;
import java.util.List;

public class SellerViewPagerAdapter extends FragmentPagerAdapter {


    private final Activity activity;
    private final List<ProductAvailabilityModel> availabilityList;
    private final SellerModel sellerModel;
    private final int position;
    private final String TAG = getClass().getSimpleName();
    private final ProductDetailModel productDetailModel;

    public SellerViewPagerAdapter(FragmentManager fm, Activity activity, List<ProductAvailabilityModel> availabilityList,
                                  SellerModel sellerModel, int position, ProductDetailModel productDetailModel) {
        super(fm);
        this.activity = activity;
        this.availabilityList = availabilityList;
        this.sellerModel = sellerModel;
        this.position = position;
        this.productDetailModel = productDetailModel;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
//        Fragment fragment = new SellerFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(MyConstants.SELLER_LIST,sellerModel);
//        fragment.setArguments(bundle);
//        CommonMethods.showLog(TAG, "Adapter Calls : " + sellerModel.getAddress());
//        return fragment;

        CommonMethods.showLog(TAG, "Av Type : " + availabilityList.get(position).getType());
        return new SellerFragment(activity, sellerModel, availabilityList.get(position), this.position, position, productDetailModel);
//        return new SellerFragment(activity, sellerModel, availabilityList.get(position), this.position, position, productDetailModel);
    }

    @Override
    public int getCount() {
        return availabilityList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        if (availabilityList.get(position).getType().equalsIgnoreCase(MyConstants.IN_STORE)) {
            title = activity.getString(R.string.inStore);
        } else if (availabilityList.get(position).getType().equalsIgnoreCase(MyConstants.DELIVER_NOW)) {
            title = activity.getString(R.string.deliverNow);
        } else {
            title = activity.getString(R.string.shipping);
        }
        return title;

//        return super.getPageTitle(tabsList.get(position));
    }
}
