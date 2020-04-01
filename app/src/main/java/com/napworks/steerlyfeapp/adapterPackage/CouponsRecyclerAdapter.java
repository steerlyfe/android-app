package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.dialogPackage.CouponConfirmationDialog;
import com.napworks.steerlyfeapp.modelPackage.CouponListInnerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CouponsRecyclerAdapter extends RecyclerView.Adapter<CouponsRecyclerAdapter.MyViewHolder> {
    private final List<CouponListInnerModel> couponList;
    private final Activity activity;
    private final CouponConfirmationDialog couponConfirmationDialog;
    private String TAG = getClass().getSimpleName();

    public CouponsRecyclerAdapter(Activity activity, List<CouponListInnerModel> couponList, CouponConfirmationDialog couponConfirmationDialog) {
        this.activity = activity;
        this.couponList = couponList;
        this.couponConfirmationDialog = couponConfirmationDialog;
        CommonMethods.showLog(TAG, "Coupons : " + couponList.size());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_coupons, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CouponListInnerModel couponListInnerModel = couponList.get(position);
        holder.couponCode.setText(couponListInnerModel.getCouponCode());

        if (couponListInnerModel.getDiscountType().equalsIgnoreCase(MyConstants.AMOUNT)) {
            holder.couponDiscount.setText(activity.getString(R.string.get).
                    concat(" ").
                    concat(activity.getString(R.string.dollarSign)).
                    concat(String.valueOf(couponListInnerModel.getDiscountAmount())).
                    concat(" ").
                    concat(activity.getString(R.string.discountForMinimumText)).
                    concat(" ").
                    concat(activity.getString(R.string.dollarSign)).
                    concat(String.valueOf(couponListInnerModel.getMoreThenAmount())));
        } else {
            holder.couponDiscount.setText(activity.getString(R.string.get).
                    concat(" ").
                    concat(String.valueOf(couponListInnerModel.getDiscountPercentage())).
                    concat("%").
                    concat(" ").
                    concat(activity.getString(R.string.discounUptoText)).
                    concat(" ").
                    concat(activity.getString(R.string.dollarSign)).
                    concat(String.valueOf(couponListInnerModel.getDiscountPercentageUptoAmount())));
        }

    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.couponDiscount)
        TextView couponDiscount;
        @BindView(R.id.couponCode)
        TextView couponCode;
        @BindView(R.id.applyCouponButton)
        TextView applyCouponButton;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            applyCouponButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.applyCouponButton:
                    CommonMethods.showLog(TAG, "Hit Api");
                    couponConfirmationDialog.showDialog(activity.getString(R.string.doYouWantToApplyCouponText),
                            MyConstants.APPLY_COUPON, couponList.get(getAdapterPosition()));
                    break;
            }
        }
    }


}
