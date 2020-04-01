package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.ProductDetailActivity;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DealsStoreDetailAdapter extends RecyclerView.Adapter<DealsStoreDetailAdapter.MyViewHolder> {
    private final int screenWidth;
    private String TAG = getClass().getSimpleName();
    private final List<ProductDetailModel> suggestedProductList;
    private final Activity activity;
    private int currentPosition = 0;

    public DealsStoreDetailAdapter(List<ProductDetailModel> suggestedProductList, Activity activity, int screenWidth) {
        this.activity = activity;
        this.suggestedProductList = suggestedProductList;
        this.screenWidth = screenWidth;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_suggested_products, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        ProductDetailModel productDetailModel = suggestedProductList.get(position);
//
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions = requestOptions.transforms(new RoundedCorners(30));
//        CommonMethods.showLog(TAG, "Image : " + productDetailModel.getImage_url());
//        Glide.with(activity).
//                load(productDetailModel.getImage_url()).
//                apply(requestOptions).
//                into((holder).image);

    }


    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.mainLinear)
        LinearLayout mainLinear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(mainLinear.getLayoutParams());
            CommonMethods.showLog(TAG, "WIDTH : " + screenWidth);
            layoutParams.width = screenWidth + 1 - 1;
            layoutParams.height = screenWidth + 1 - 1;
            mainLinear.setLayoutParams(layoutParams);

            mainLinear.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currentPosition = getAdapterPosition();
            if (currentPosition >= 0) {
                ProductDetailModel productDetailModel = suggestedProductList.get(currentPosition);
                if (v.getId() == R.id.mainLinear) {
                    Intent intent = new Intent(activity, ProductDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra(MyConstants.PRODUCT_ID, productDetailModel.getProduct_id());
//                    intent.putExtra(MyConstants.PRODUCT_ADDITIONAL_FEATURES, "");

                    activity.startActivity(intent);
                    activity.overridePendingTransition(0, 0);
                }
            }
        }
    }
}
