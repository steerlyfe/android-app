package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.modelPackage.OrderDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailsRecyclerAdapter extends RecyclerView.Adapter<OrderDetailsRecyclerAdapter.MyViewHolder> {
    private CommonMessageDialog commonMessageDialog;
    private Activity activity;
    private List<OrderDetailModel> orderDetailList;
    private int currentPosition = 0;
    private String TAG = getClass().getSimpleName();

    public OrderDetailsRecyclerAdapter(Activity activity, List<OrderDetailModel> orderDetailList, CommonMessageDialog commonMessageDialog) {
        this.activity = activity;
        this.commonMessageDialog = commonMessageDialog;
        this.orderDetailList = orderDetailList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderDetailModel orderDetailModel = orderDetailList.get(position);
        CommonMethods.showLog(TAG, "AF PRICE : " + orderDetailModel.getAdditional_feature_price());
        double price = orderDetailModel.getSale_price() + orderDetailModel.getAdditional_feature_price();
        double finalPrice = orderDetailModel.getQuantity() * price;
        finalPrice = finalPrice + orderDetailModel.getProduct_availability_price();
        holder.productName.setText(orderDetailModel.getProduct_name().
                concat(" (").
                concat(orderDetailModel.getAdditional_feature()).
                concat(")"));
        holder.sellerName.setText(orderDetailModel.getStore_name());
        holder.productPrice.setText(activity.getString(R.string.dollarSign).concat(" ").concat(String.valueOf(orderDetailModel.getSale_price())));
        holder.productQuantity.setText(String.valueOf(orderDetailModel.getQuantity()));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(20));
        Glide.with(activity)
                .asBitmap()
                .load(orderDetailModel.getProduct_image())
                .apply(requestOptions)
                .into(holder.productImage);
        holder.totalCost.setText(activity.getString(R.string.dollarSign).concat(" ").concat(String.valueOf(finalPrice)));



    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.totalCost)
        TextView totalCost;
        @BindView(R.id.sellerName)
        TextView sellerName;
        @BindView(R.id.productName)
        TextView productName;
        @BindView(R.id.productQuantity)
        TextView productQuantity;
        @BindView(R.id.productPrice)
        TextView productPrice;
        @BindView(R.id.deliveryPrice)
        TextView deliveryPrice;
        @BindView(R.id.productImage)
        ImageView productImage;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
