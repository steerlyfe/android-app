package com.napworks.steerlyfeapp.designsPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.ProductDetailActivity;
import com.napworks.steerlyfeapp.modelPackage.OrderDetailModel;
import com.napworks.steerlyfeapp.modelPackage.OrderLogModel;
import com.napworks.steerlyfeapp.modelPackage.OrderStatusModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;
import com.transferwise.sequencelayout.SequenceStep;

import java.util.List;


public class CustomOrderInfoContainer {

    private final Activity activity;

    private final LinearLayout inflatedOrderInfoLayout;

    double subTotalPrice = 0.0;

    private List<OrderDetailModel> orderInfoList;
    private String TAG = getClass().getSimpleName();
//    public List<OrderStatusModel> orderStatusList;


    public CustomOrderInfoContainer(Activity activity, List<OrderDetailModel> orderInfoList, LinearLayout inflatedOrderInfoLayout) {
        this.activity = activity;
        this.orderInfoList = orderInfoList;
        this.inflatedOrderInfoLayout = inflatedOrderInfoLayout;
//        orderStatusList = new ArrayList<>();
        setContainerDetail();
    }


    private void setContainerDetail() {
        for (int i = 0; i < orderInfoList.size(); i++) {
            addProductInfoDataInContainer(i, orderInfoList.get(i), orderInfoList.get(i).getStatus());
        }
    }

    private void addProductInfoDataInContainer(int position, OrderDetailModel orderDetailModel, OrderStatusModel orderStatusModel) {
        if (orderDetailModel != null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mainView = inflater.inflate(R.layout.layout_order_detail, inflatedOrderInfoLayout, false);

            double price = (orderDetailModel.getSale_price() + orderDetailModel.getAdditional_feature_price()) * orderDetailModel.getQuantity();
            price = CommonMethods.roundOff(price);

            TextView itemTotalCost = mainView.findViewById(R.id.itemTotalCost);
            TextView lastUpdatedAt = mainView.findViewById(R.id.lastUpdatedAt);
            TextView orderStatus = mainView.findViewById(R.id.orderStatus);
            TextView productName = mainView.findViewById(R.id.productName);
            TextView productQuantity = mainView.findViewById(R.id.productQuantity);
            TextView productPrice = mainView.findViewById(R.id.productPrice);
            TextView buyAgain = mainView.findViewById(R.id.buyAgain);
            TextView writeAReviewText = mainView.findViewById(R.id.writeAReviewText);
            TextView costWithQuantity = mainView.findViewById(R.id.costWithQuantity);
            LinearLayout inStoreStepLayout = mainView.findViewById(R.id.inStoreStepLayout);
            LinearLayout stepLayout = mainView.findViewById(R.id.stepLayout);
            ImageView productImage = mainView.findViewById(R.id.productImage);

            SequenceStep orderPlacedStep = mainView.findViewById(R.id.orderPlacedStep);
            SequenceStep processingStep = mainView.findViewById(R.id.processingStep);
            SequenceStep deliveredStep = mainView.findViewById(R.id.deliveredStep);
            SequenceStep dispatchedStep = mainView.findViewById(R.id.dispatchedStep);
            SequenceStep inStoreProcessingStep = mainView.findViewById(R.id.inStoreProcessingStep);
            SequenceStep inStoreOrderPlacedStep = mainView.findViewById(R.id.inStoreOrderPlacedStep);
            SequenceStep inStoreReadyToPickStep = mainView.findViewById(R.id.inStoreReadyToPickStep);

            productName.setText(orderDetailModel.getProduct_name().
                    concat(" (").
                    concat(orderDetailModel.getAdditional_feature()).
                    concat(")"));
            productPrice.setText(activity.getString(R.string.dollarSign).concat(String.valueOf(orderDetailModel.getSale_price() + orderDetailModel.getAdditional_feature_price())));
            productQuantity.setText(String.valueOf(orderDetailModel.getQuantity()));
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new RoundedCorners(20));
            Glide.with(activity)
                    .asBitmap()
                    .load(orderDetailModel.getProduct_image())
                    .apply(requestOptions)
                    .into(productImage);
            costWithQuantity.setText(activity.getString(R.string.dollarSign).
                    concat(String.valueOf(orderDetailModel.getSale_price() + orderDetailModel.getAdditional_feature_price())).
                    concat(" * ").
                    concat(String.valueOf(orderDetailModel.getQuantity())));
            itemTotalCost.setText(activity.getString(R.string.dollarSign).concat(String.valueOf(price)));

            if (orderDetailModel.getProduct_availability().equalsIgnoreCase(MyConstants.IN_STORE)) {
                stepLayout.setVisibility(View.GONE);
                inStoreStepLayout.setVisibility(View.VISIBLE);
            } else {
                stepLayout.setVisibility(View.VISIBLE);
                inStoreStepLayout.setVisibility(View.GONE);
            }

//            orderInfoList.get(0).getStatus().setCurrentStatus(MyConstants.PRODUCT_RECEIVED);
//            orderInfoList.get(1).getStatus().setCurrentStatus(MyConstants.DELIVERED);

            for (int i = 0; i < orderStatusModel.getLogsList().size(); i++) {
                OrderLogModel logModel = orderStatusModel.getLogsList().get(i);
//                if (logModel.isValue()) {
//                    CommonMethods.showLog(TAG, "Value True At : " + i);
//                    if (logModel.getType().equalsIgnoreCase(MyConstants.ORDER_PLACED)) {
//                        orderPlacedStep.setActive(true);
//                        orderPlacedStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
//                        inStoreOrderPlacedStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
//                        inStoreOrderPlacedStep.setActive(true);
//
//                    } else if (logModel.getType().equalsIgnoreCase(MyConstants.PROCESSING)) {
//                        processingStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
//                        orderPlacedStep.setActive(false);
//                        processingStep.setActive(true);
//
//                    } else if (logModel.getType().equalsIgnoreCase(MyConstants.DISPATCHED)) {
//                        dispatchedStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
//                        orderPlacedStep.setActive(false);
//                        processingStep.setActive(false);
//                        dispatchedStep.setActive(true);
//                    } else if (logModel.getType().equalsIgnoreCase(MyConstants.DELIVERED)) {
//                        deliveredStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
//                        orderPlacedStep.setActive(false);
//                        processingStep.setActive(false);
//                        dispatchedStep.setActive(false);
//                        deliveredStep.setActive(true);
//
//                    } else if (logModel.getType().equalsIgnoreCase(MyConstants.READY_TO_PICK)) {
//                        inStoreOrderPlacedStep.setActive(false);
//                        inStoreReadyToPickStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
//                        inStoreReadyToPickStep.setActive(true);
//
//                    } else {
//                        inStoreOrderPlacedStep.setActive(false);
//                        inStoreReadyToPickStep.setActive(false);
//                        inStoreProcessingStep.setAnchor(CommonMethods.getOrderDate(logModel.getTime()));
//                        inStoreProcessingStep.setActive(true);
//
//                    }
//                }

                if (orderStatusModel.getCurrentStatus().equalsIgnoreCase(logModel.getType())) {
                    lastUpdatedAt.setText(activity.getString(R.string.lastUpdatedAt).concat(" ").concat(CommonMethods.getDate(logModel.getTime())));

                    if (orderStatusModel.getCurrentStatus().equalsIgnoreCase(MyConstants.ORDER_PLACED)) {
                        orderStatus.setText(activity.getString(R.string.orderPlaced));
                        orderPlacedStep.setActive(true);
                        orderPlacedStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
                        inStoreOrderPlacedStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
                        inStoreOrderPlacedStep.setActive(true);
                    }
                    if (logModel.getType().equalsIgnoreCase(MyConstants.PROCESSING)) {
                        processingStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
                        processingStep.setActive(true);
                        orderStatus.setText(activity.getString(R.string.processing));
                    }
                    if (logModel.getType().equalsIgnoreCase(MyConstants.DISPATCHED)) {
                        dispatchedStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
                        dispatchedStep.setActive(true);
                        orderStatus.setText(activity.getString(R.string.dispatched));
                    }
                    if (logModel.getType().equalsIgnoreCase(MyConstants.DELIVERED)) {
                        deliveredStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
                        deliveredStep.setActive(true);
                        orderStatus.setText(activity.getString(R.string.delivered));
                    }
                    if (logModel.getType().equalsIgnoreCase(MyConstants.READY_TO_PICK)) {
                        orderStatus.setText(activity.getString(R.string.readyToPick));
                        inStoreReadyToPickStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
                        inStoreReadyToPickStep.setActive(true);
                    }
                    if (logModel.getType().equalsIgnoreCase(MyConstants.PRODUCT_RECEIVED)) {
                        orderStatus.setText(activity.getString(R.string.productReceived));
                        inStoreProcessingStep.setAnchor(CommonMethods.getOrderStatusDate(logModel.getTime()));
                        inStoreProcessingStep.setActive(true);
                    }
                }

                buyAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(activity, ProductDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra(MyConstants.PRODUCT_ID, orderDetailModel.getProduct_id());
//                        intent.putExtra(MyConstants.PRODUCT_ADDITIONAL_FEATURES, orderDetailModel.getAdditional_feature());
                        activity.startActivity(intent);

                    }
                });
            }
            inflatedOrderInfoLayout.addView(mainView);
        }
    }
}
