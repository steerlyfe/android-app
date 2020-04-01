package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class BuyOrdersAgainAdapter extends RecyclerView.Adapter<BuyOrdersAgainAdapter.MyViewHolder> {
    private final int screenWidth;
    private String TAG = getClass().getSimpleName();
    private final List<ProductDetailModel> productList;
    private final Activity activity;
    private int currentPosition = 0;

    public BuyOrdersAgainAdapter(List<ProductDetailModel> productList, Activity activity, int screenWidth) {
        this.activity = activity;
        this.productList = productList;
        this.screenWidth = screenWidth;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_buy_orders_again, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductDetailModel trendingProductModel = productList.get(position);
        holder.productName.setText(trendingProductModel.getProduct_name());
        holder.productPrice.setText(activity.getString(R.string.dollarSign).
                concat(" ").
                concat(String.valueOf(trendingProductModel.getSale_price())));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(30));
        CommonMethods.showLog(TAG, "Image : " + trendingProductModel.getImage_url());
        Glide.with(activity).
                load(trendingProductModel.getImage_url()).
                apply(requestOptions).
                into((holder).image);

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.productName)
        TextView productName;
        @BindView(R.id.productPrice)
        TextView productPrice;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.addImageView)
        ImageView addImageView;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.mainLinear)
        LinearLayout mainLinear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(image.getLayoutParams());
            layoutParams.width = screenWidth + 1 - 1;
            layoutParams.height = screenWidth + 1 - 1;
            image.setLayoutParams(layoutParams);

            mainLinear.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currentPosition = getAdapterPosition();
            if (currentPosition >= 0) {
                ProductDetailModel productDetailModel = productList.get(currentPosition);
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
