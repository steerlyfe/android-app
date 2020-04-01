package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.CouponsRecyclerAdapter;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.CouponConfirmationDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.AppliedCouponResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.ConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.CouponConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.CouponListResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CouponAppliedModel;
import com.napworks.steerlyfeapp.modelPackage.CouponListInnerModel;
import com.napworks.steerlyfeapp.modelPackage.CouponsListMainModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CouponsActivity extends ParentActivity implements CouponListResponseInterface, CouponConfirmationDialogResponseInterface, AppliedCouponResponseInterface {
    public String TAG = getClass().getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarTextView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.mainLinear)
    FrameLayout mainLinear;

    private String storeList;
    private String orderInfoList;

    SharedPreferences sharedPreferences;
    LoadingDialog loadingDialog;
    CommonMessageDialog commonMessageDialog;
    private List<CouponListInnerModel> couponList;
    private CouponsRecyclerAdapter couponsRecyclerAdapter;
    CouponConfirmationDialog confirmationDialog;
    double totalAmount = 0.0;

    CouponListInnerModel couponListInnerModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            storeList = getIntent().getStringExtra(MyConstants.STORE_LIST);
            orderInfoList = getIntent().getStringExtra(MyConstants.ORDER_INFO);
            totalAmount = getIntent().getDoubleExtra(MyConstants.TOTAL_AMOUNT, 0.0);
        }
        CommonMethods.showLog(TAG, "Amount : " + totalAmount);
        couponList = new ArrayList<>();
        confirmationDialog = new CouponConfirmationDialog(this, this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.availableCoupons), toolbarTextView);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        commonMessageDialog = new CommonMessageDialog(this);
        CommonMethods.showLog(TAG, "Store List Size : " + storeList + " OrderInfo List : " + orderInfoList);
        couponListInnerModel = new CouponListInnerModel();

        couponsRecyclerAdapter = new CouponsRecyclerAdapter(this, couponList, confirmationDialog);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(couponsRecyclerAdapter);

        mainLinear.setVisibility(View.GONE);
        hitApi();
    }

    /*
     * Get Coupon List Api
     */
    private void hitApi() {
        loadingDialog.showDialog();
        CommonWebServices.getCouponList(sharedPreferences, storeList, this);
    }

    /*
     * Get Coupon List Api Resppnse
     */

    @Override
    public void getCouponListResponse(String status, CouponsListMainModel couponsListMainModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (couponsListMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "CouponList Api Status : " + couponsListMainModel.getStatus());
                    switch (couponsListMainModel.getStatus()) {
                        case "1":
                            mainLinear.setVisibility(View.VISIBLE);
                            CommonMethods.showLog(TAG, "CouponList Api Success");
                            couponList.addAll(couponsListMainModel.getCouponList());
                            CommonMethods.showLog(TAG, "CouponList Size : " + couponList.size());
                            couponsRecyclerAdapter.notifyDataSetChanged();
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "CouponList Status 2 : " + couponsListMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(couponsListMainModel.getMessage());
                            break;
                    }
                } else {
                    commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                }
                break;
            case MyConstants.NULL_RESPONSE:
            case MyConstants.FAILURE_RESPONSE:
                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                break;
        }
    }

    /*
     * Confirmation Dialog Response
     */
    @Override
    public void getCouponConfirmationDialogResponse(String type, CouponListInnerModel couponListInnerModel) {

        if (type.equalsIgnoreCase(MyConstants.APPLY_COUPON)) {
            this.couponListInnerModel = couponListInnerModel;
            CommonMethods.showLog(TAG, "COUPON ID : " + couponListInnerModel.getCouponId() + " Amount : " + totalAmount);
            loadingDialog.showDialog();
            CommonWebServices.checkCouponWithOrder(sharedPreferences,
                    String.valueOf(totalAmount),
                    orderInfoList,
                    couponListInnerModel.getCouponId(),
                    this);
        }
    }

    /*
     * Applied Coupon Api Response
     */

    @Override
    public void getAppliedCouponResponse(String status, CouponAppliedModel couponAppliedModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (couponAppliedModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "AppliedCoupon Api Status : " + couponAppliedModel.getStatus() + "Message : " + couponAppliedModel.getMessage());
                    switch (couponAppliedModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "AppliedCoupon Api Success");
                            Intent intent = new Intent();
                            intent.putExtra(MyConstants.COUPON_DATA, couponAppliedModel);
                            intent.putExtra(MyConstants.COUPON_FULL_DATA, couponListInnerModel);
                            setResult(RESULT_OK, intent);
                            finish();
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "AppliedCoupon Status 2 : " + couponAppliedModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(couponAppliedModel.getMessage());
                            break;
                    }
                } else {
                    commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                }
                break;
            case MyConstants.NULL_RESPONSE:
            case MyConstants.FAILURE_RESPONSE:
                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                break;
        }
    }
}
