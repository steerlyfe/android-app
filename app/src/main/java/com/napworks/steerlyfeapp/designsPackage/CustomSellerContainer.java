package com.napworks.steerlyfeapp.designsPackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.BarcodeProductDetailActivity;
import com.napworks.steerlyfeapp.activitiesPackage.ProductDetailActivity;
import com.napworks.steerlyfeapp.adapterPackage.SellerViewPagerAdapter;
import com.napworks.steerlyfeapp.modelPackage.AdditionalFeaturesModel;
import com.napworks.steerlyfeapp.modelPackage.ProductAvailabilityModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.SellerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.ArrayList;
import java.util.List;


public class CustomSellerContainer {

    private final Activity activity;
    private final List<ProductAvailabilityModel> availabilityList;
    private final ProductDetailModel productDetailModel;
    private List<SellerModel> sellerList;
    private final FragmentManager supportFragmentManager;


    private final LinearLayout inflatedSellerLayout;
    private final List<AdditionalFeaturesModel> additionalFeaturesList;
    private String TAG = getClass().getSimpleName();

    public CustomSellerContainer(FragmentManager supportFragmentManager, Activity activity, List<AdditionalFeaturesModel> additionalFeaturesList, LinearLayout inflatedSellerLayout,
                                 List<ProductAvailabilityModel> availabilityList, ProductDetailModel productDetailModel) {
        this.activity = activity;
        this.supportFragmentManager = supportFragmentManager;
        this.additionalFeaturesList = additionalFeaturesList;
        this.availabilityList = availabilityList;
        this.inflatedSellerLayout = inflatedSellerLayout;
        this.productDetailModel = productDetailModel;
        sellerList = new ArrayList<>();
        setContainerDetail();
    }


    private void setContainerDetail() {
        for (int i = 0; i < additionalFeaturesList.size(); i++) {
            AdditionalFeaturesModel innerModel = additionalFeaturesList.get(i);
            if (innerModel.isSelected()) {
                for (int j = 0; j < innerModel.getSellers().size(); j++) {
                    SellerModel innerSellerModel = innerModel.getSellers().get(j);
                    innerSellerModel.setSelectedUnit(innerModel.getValue().concat(" ").concat(innerModel.getUnitType()));
                    innerSellerModel.setSelectedUnitPrice(innerModel.getPrice());
                    sellerList.add(innerSellerModel);
                }
            }
        }

        if (sellerList.size() > 0) {
            for (int j = 0; j < sellerList.size(); j++) {
                SellerModel innerSellerModel = sellerList.get(j);
                addSellerDataInContainer(innerSellerModel, j);
            }
        } else {
            CommonMethods.showLog(TAG, "No Seller Available");
        }
    }

    private void addSellerDataInContainer(SellerModel sellerModel, int position) {
        if (!sellerModel.getAddress().isEmpty()) {
            CommonMethods.showLog(TAG, "Selected : " + sellerModel.getSelectedUnit());
            CommonMethods.showLog(TAG, "SubStore : " + sellerModel.getSub_store_id());
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mainView = inflater.inflate(R.layout.container_seller, inflatedSellerLayout, false);
            LinearLayout linearLayout = mainView.findViewById(R.id.linearLayout);
            ViewPager viewPager = mainView.findViewById(R.id.viewPager);
            TabLayout tabLayout = mainView.findViewById(R.id.tabLayout);
            viewPager.setId((int) (Math.random() * 10000));
            viewPager.setOffscreenPageLimit(0);

            if (activity instanceof ProductDetailActivity) {

                SellerViewPagerAdapter sellerViewPagerAdapter = new SellerViewPagerAdapter(((ProductDetailActivity) activity).getActivityFragmentManager(), activity,
                        availabilityList,
                        sellerModel,
                        position,
                        productDetailModel);
                tabLayout.setupWithViewPager(viewPager);
                viewPager.setAdapter(sellerViewPagerAdapter);
                CommonMethods.showLog(TAG, "Position :" + position);
            }
//            else {
//                SellerViewPagerAdapter sellerViewPagerAdapter = new SellerViewPagerAdapter(((BarcodeProductDetailActivity) activity).getActivityFragmentManager(), activity,
//                        availabilityList,
//                        sellerModel,
//                        position,
//                        productDetailModel);
//                tabLayout.setupWithViewPager(viewPager);
//                viewPager.setAdapter(sellerViewPagerAdapter);
//                CommonMethods.showLog(TAG, "Position :" + position);
//
//            }

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethods.showLog(TAG, "Clicked on : " + position);
                }
            });
            inflatedSellerLayout.addView(mainView);
        }
    }

//    private void setContainerDetail() {
//        for (int i = 0; i < additionalFeaturesList.size(); i++) {
//            AdditionalFeaturesModel innerModel = additionalFeaturesList.get(i);
//            addSellerDataInContainer(innerModel.getSellers(), i, innerModel.isSelected());
//        }
//    }

//    private void addSellerDataInContainer(List<SellerModel> sellersList, int position, boolean selectedUnit) {
//        if (selectedUnit) {
//            CommonMethods.showLog(TAG, "List Size : " + sellersList.size());
//            for (int i = 0; i < sellersList.size(); i++) {
//                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                final View mainView = inflater.inflate(R.layout.container_seller, inflatedSellerLayout, false);
//                LinearLayout linearLayout = mainView.findViewById(R.id.linearLayout);
//                ViewPager viewPager = mainView.findViewById(R.id.viewPager);
//                TabLayout tabLayout = mainView.findViewById(R.id.tabLayout);
//                SellerViewPagerAdapter sellerViewPagerAdapter = new SellerViewPagerAdapter(supportFragmentManager, activity,
//                        availabilityList,
//                        sellersList.get(i).getAddress());
//                tabLayout.setupWithViewPager(viewPager);
//                viewPager.setAdapter(sellerViewPagerAdapter);
//                CommonMethods.showLog(TAG, "Position :" + position);
//
//                linearLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CommonMethods.showLog(TAG, "Clicked on : " + position);
//                    }
//                });
//                inflatedSellerLayout.addView(mainView);
//
//            }
//        }
//    }
}
