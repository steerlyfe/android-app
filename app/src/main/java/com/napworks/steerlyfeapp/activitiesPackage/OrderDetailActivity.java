package com.napworks.steerlyfeapp.activitiesPackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.designsPackage.CustomOrderInfoContainer;
import com.napworks.steerlyfeapp.modelPackage.OrderDetailModel;
import com.napworks.steerlyfeapp.modelPackage.OrderHistoryInnerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends ParentActivity {

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarText;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.shippingAddress)
    TextView shippingAddress;
    @BindView(R.id.shippingPrice)
    TextView shippingPrice;
    //    @BindView(R.id.placedOn)
//    TextView placedOn;
//    @BindView(R.id.orderStatus)
//    TextView orderStatus;
    @BindView(R.id.subtotalPrice)
    TextView subtotalPrice;
    @BindView(R.id.taxPrice)
    TextView taxPrice;
    @BindView(R.id.savingsPrice)
    TextView savingsPrice;
    @BindView(R.id.couponDiscountPrice)
    TextView couponDiscountPrice;
    @BindView(R.id.totalCost)
    TextView totalCost;
    @BindView(R.id.inflatedOrderInfoLayout)
    LinearLayout inflatedOrderInfoLayout;
    @BindView(R.id.shippingAddressLayout)
    LinearLayout shippingAddressLayout;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;

    OrderHistoryInnerModel orderDetailInnerModel;
    private List<OrderDetailModel> orderDetailList;
    private String type = "";
    boolean isShippingAddressAvailable = false;
    double shippingValue = 0.0;
    private MenuItem rateNow;
    private boolean isShowRating = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        orderDetailList = new ArrayList<>();

        if (getIntent() != null) {
            orderDetailInnerModel = (OrderHistoryInnerModel) getIntent().getSerializableExtra(MyConstants.ORDERS);
            orderDetailList.addAll(orderDetailInnerModel.getOrder_info());
            type = getIntent().getStringExtra(MyConstants.TYPE);
        }
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.orderDetails), toolbarText);
        setData();

        inflatedOrderInfoLayout.removeAllViews();
        if (orderDetailList.size() > 0) {
            new CustomOrderInfoContainer(this, orderDetailList, inflatedOrderInfoLayout);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        CommonMethods.showLog(TAG, "On Create Options Menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_rating_menu, menu);
        rateNow = menu.findItem(R.id.rateNow);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        CommonMethods.showLog(TAG, "On Prepare Options Menu");
        rateNow = menu.findItem(R.id.rateNow);
        if (isShowRating) {
            rateNow.setVisible(true);
        } else {
            rateNow.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.rateNow) {
            CommonMethods.showLog(TAG, "Start Button Clicked ");

            Intent intent = new Intent(this, RatingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(MyConstants.CALLED_FROM, MyConstants.ORDER_DETAIL_ACTIVITY);
            intent.putExtra(MyConstants.ORDER_ID, orderDetailInnerModel.getOrder_id());
            startActivityForResult(intent, MyConstants.RATE_ORDER);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyConstants.RATE_ORDER) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(0, 0);
            }
        }
    }
    /*
     * Set Data
     */
    private void setData() {
        for (int i = 0; i < orderDetailList.size(); i++) {
            OrderDetailModel innerModel = orderDetailList.get(i);
            if (innerModel.getProduct_availability().equalsIgnoreCase(MyConstants.DELIVER_NOW) ||
                    innerModel.getProduct_availability().equalsIgnoreCase(MyConstants.SHIPPING)) {
                isShippingAddressAvailable = true;
            }
            shippingValue = shippingValue + innerModel.getProduct_availability_price();

            if (innerModel.getStatus().getCurrentStatus().equalsIgnoreCase(MyConstants.DELIVERED) ||
                    innerModel.getStatus().getCurrentStatus().equalsIgnoreCase(MyConstants.PRODUCT_RECEIVED)) {
                if (orderDetailInnerModel.getOrder_rating() == 0.0) {
                    isShowRating = true;
                }
            }
        }


        if (isShippingAddressAvailable) {
            shippingAddressLayout.setVisibility(View.VISIBLE);
            shippingAddress.setText(CommonMethods.getFullAddress(this, orderDetailInnerModel.getAddress_detail()));
        } else {
            shippingAddressLayout.setVisibility(View.GONE);
        }

        orderId.setText(getString(R.string.orderCaps)
                .concat("  #  ")
                .concat(orderDetailInnerModel.getOrder_id())
                .concat("  .  ")
                .concat(getString(R.string.placedOnCaps))
                .concat("  ")
                .concat(CommonMethods.getOrderHistoryDate(orderDetailInnerModel.getOrder_time())));

        double totalDiscount = orderDetailInnerModel.getCoupon_discount() + orderDetailInnerModel.getDiscount_amount();
        totalDiscount = CommonMethods.roundOff(totalDiscount);

        double subTotal = orderDetailInnerModel.getTotal_amount() - shippingValue;

        subtotalPrice.setText(getString(R.string.dollarSign).concat(String.valueOf(CommonMethods.roundOff(subTotal))));
        couponDiscountPrice.setText(getString(R.string.dollarSign).concat(String.valueOf(CommonMethods.roundOff(totalDiscount))));
//        taxPrice.setText(getString(R.string.dollarSign).concat(String.valueOf(totalDiscount)));
        savingsPrice.setText(getString(R.string.dollarSign).concat(String.valueOf(CommonMethods.roundOff(orderDetailInnerModel.getTotal_savings()))));
        shippingPrice.setText(getString(R.string.dollarSign).concat(String.valueOf(CommonMethods.roundOff(shippingValue))));
        totalCost.setText(getString(R.string.dollarSign).concat(String.valueOf(CommonMethods.roundOff(orderDetailInnerModel.getAmount_paid()))));


//        if (orderDetailInnerModel.getOrder_status().equalsIgnoreCase(MyConstants.PENDING)) {
//            orderStatus.setText(getString(R.string.pending));
//            orderStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
//        } else if (orderDetailInnerModel.getOrder_status().equalsIgnoreCase(MyConstants.COMPLETED)) {
//            orderStatus.setText(getString(R.string.completed));
//            orderStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
//        } else {
//            orderStatus.setText(getString(R.string.cancelled));
//            orderStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
//        }
    }
}
