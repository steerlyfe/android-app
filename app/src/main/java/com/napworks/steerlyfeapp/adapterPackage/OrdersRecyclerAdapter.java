package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.OrderDetailActivity;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.modelPackage.OrderHistoryInnerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter {
    private CommonMessageDialog commonMessageDialog;
    private Activity activity;
    private List<OrderHistoryInnerModel> ordersList;
    private int currentPosition = 0;

    public OrdersRecyclerAdapter(Activity activity, List<OrderHistoryInnerModel> ordersList, CommonMessageDialog commonMessageDialog) {
        this.activity = activity;
        this.commonMessageDialog = commonMessageDialog;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orders, parent, false);
            return new DataClass(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_layout_swipe, parent, false);
            return new ProgressClass(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!ordersList.get(position).isLoadingType()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataClass) {
            OrderHistoryInnerModel orderHistoryInnerModel = ordersList.get(position);
            ((DataClass) holder).orderId.setText(activity.getString(R.string.order).concat("  #  ").concat(orderHistoryInnerModel.getOrder_id()));
            ((DataClass) holder).orderDate.setText(activity.getString(R.string.placed)
                    .concat("  ")
                    .concat(CommonMethods.getOrderHistoryDate(orderHistoryInnerModel.getOrder_time())));
            ((DataClass) holder).noOfItems.setText("$"
                    .concat(String.valueOf(CommonMethods.roundOff(orderHistoryInnerModel.getAmount_paid())))
                    .concat("  .  ")
                    .concat(String.valueOf(orderHistoryInnerModel.getOrder_info().size()))
                    .concat(" ")
                    .concat(activity.getString(R.string.items)));

            if (orderHistoryInnerModel.getOrder_status().equalsIgnoreCase(MyConstants.PENDING)) {
                ((DataClass) holder).orderStatus.setText(activity.getString(R.string.pending));
                ((DataClass) holder).orderStatus.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_orange_dark));

            } else if (orderHistoryInnerModel.getOrder_status().equalsIgnoreCase(MyConstants.COMPLETED)) {
                ((DataClass) holder).orderStatus.setText(activity.getString(R.string.completed));
                ((DataClass) holder).orderStatus.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_green_dark));

            } else {
                ((DataClass) holder).orderStatus.setText(activity.getString(R.string.cancelled));
                ((DataClass) holder).orderStatus.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_red_dark));

            }


            if (orderHistoryInnerModel.getOrder_rating() == 0.0) {
                ((DataClass) holder).rightLayout.setVisibility(View.GONE);
            } else {
                ((DataClass) holder).rightLayout.setVisibility(View.VISIBLE);
                ((DataClass) holder).orderRating.setText(String.valueOf(orderHistoryInnerModel.getOrder_rating()));
            }


        } else if (holder instanceof ProgressClass) {
            ((ProgressClass) holder).progressLayout.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class DataClass extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.orderId)
        TextView orderId;
        @BindView(R.id.orderDate)
        TextView orderDate;
        @BindView(R.id.noOfItems)
        TextView noOfItems;
        @BindView(R.id.orderStatus)
        TextView orderStatus;
        @BindView(R.id.rightLayout)
        CardView rightLayout;
        @BindView(R.id.orderRating)
        TextView orderRating;
        @BindView(R.id.mainLinear)
        RelativeLayout mainLinear;

        DataClass(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mainLinear.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currentPosition = getAdapterPosition();
            if (v.getId() == R.id.mainLinear) {
                Intent intent = new Intent(activity, OrderDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(MyConstants.ORDERS, ordersList.get(currentPosition));
                intent.putExtra(MyConstants.TYPE, MyConstants.ORDER_DATA);
                activity.startActivityForResult(intent,MyConstants.ORDER_DETAIL);
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    class ProgressClass extends RecyclerView.ViewHolder {
        @BindView(R.id.progressLayout)
        ProgressBar progressLayout;

        ProgressClass(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
