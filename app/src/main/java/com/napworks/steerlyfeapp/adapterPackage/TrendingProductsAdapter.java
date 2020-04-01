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
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.interfacePackage.OnTrendingProductsClickedInterFace;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrendingProductsAdapter extends RecyclerView.Adapter<TrendingProductsAdapter.MyViewHolder> {
    private final DatabaseMethods databaseMethods;
    private final OnTrendingProductsClickedInterFace onTrendingProductsClickedInterFace;
    private int screenWidth;
    private String TAG = getClass().getSimpleName();
    private final List<ProductDetailModel> trendingProductList;
    private final Activity activity;
    String type = "";

    public TrendingProductsAdapter(List<ProductDetailModel> trendingProductList, Activity activity, int screenWidth,
                                   DatabaseMethods databaseMethods, OnTrendingProductsClickedInterFace onTrendingProductsClickedInterFace) {
        this.activity = activity;
        this.trendingProductList = trendingProductList;
        this.screenWidth = screenWidth;
        this.databaseMethods = databaseMethods;
        this.onTrendingProductsClickedInterFace = onTrendingProductsClickedInterFace;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_trending_products, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductDetailModel trendingProductModel = trendingProductList.get(position);
        holder.productName.setText(trendingProductModel.getProduct_name());
        holder.productPrice.setText(activity.getString(R.string.dollarSign).
                concat(" ").
                concat(String.valueOf(trendingProductModel.getSale_price())));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(30));
        CommonMethods.showLog(TAG, "Image : " + trendingProductModel.getImage_url());
        Glide.with(activity).
                load(trendingProductList.get(position).getImage_url()).
                apply(requestOptions).
                into((holder).image);

        if (databaseMethods.getAllFavouriteProductCount() > 0) {
            if (databaseMethods.productExistsInFavourite(trendingProductModel.getProduct_id())) {
                holder.addImageView.setImageResource(R.drawable.favourite_filled);
            } else {
                holder.addImageView.setImageResource(R.drawable.favourite_colored);
            }
        } else {
            holder.addImageView.setImageResource(R.drawable.favourite_colored);
        }

    }


    @Override
    public int getItemCount() {
        return trendingProductList.size();
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


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            CommonMethods.showLog(TAG, "A WIDTH : " + screenWidth);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(image.getLayoutParams());
            layoutParams.width = screenWidth + 1 - 1;
            layoutParams.height = screenWidth + 1 - 1;
            image.setLayoutParams(layoutParams);

            addImageView.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() >= 0) {
                ProductDetailModel productDetailModel = trendingProductList.get(getAdapterPosition());
                if (v.getId() == R.id.addImageView) {
//                    databaseMethods.addOrUpdate(productDetailModel, true);
                    CommonMethods.showLog(TAG, "Fav Product Table : " + databaseMethods.getAllFavouriteProductCount());
//                    databaseMethods.insertFavouriteProduct(productDetailModel);
                    if (databaseMethods.getAllFavouriteProductCount() > 0) {
                        if (databaseMethods.productExistsInFavourite(productDetailModel.getProduct_id())) {
                            CommonMethods.showLog(TAG, "Product Delete Karo ");
                            databaseMethods.deleteParticularFavoriteProduct(productDetailModel.getProduct_id());
                            addImageView.setImageResource(R.drawable.favourite_colored);
                            type = MyConstants.PRODUCT_REMOVED;
                        } else {
                            CommonMethods.showLog(TAG, "Database Ch Heni , Product Add Karo ");
                            databaseMethods.insertFavouriteProduct(productDetailModel);
                            addImageView.setImageResource(R.drawable.favourite_filled);
                            type = MyConstants.PRODUCT_ADDED;
                        }
                    } else {
                        CommonMethods.showLog(TAG, "Database Khali , Product Add Karo ");
                        databaseMethods.insertFavouriteProduct(productDetailModel);
                        addImageView.setImageResource(R.drawable.favourite_filled);
                        type = MyConstants.PRODUCT_ADDED;
                    }
                    onTrendingProductsClickedInterFace.onTrendingProductsClicked(getAdapterPosition(), type);
                } else if (v.getId() == R.id.cardView) {
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
