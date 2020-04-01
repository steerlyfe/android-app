package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.MainActivity;
import com.napworks.steerlyfeapp.activitiesPackage.ProductDetailActivity;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.interfacePackage.OnRecyclerItemClickedInterFace;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final DatabaseMethods databaseMethods;
    private final String callFrom;
    private final OnRecyclerItemClickedInterFace onRecyclerItemClickedInterFace;
    private CommonMessageDialog commonMessageDialog;
    private Activity activity;
    private List<ProductDetailModel> productList;
    private String TAG = getClass().getSimpleName();
    private int currentPosition = 0;
    double costValue = 0.0;

    public FavoritesRecyclerAdapter(Activity activity, List<ProductDetailModel> productList,
                                    CommonMessageDialog commonMessageDialog, DatabaseMethods databaseMethods, String callFrom,
                                    double costValue,
                                    OnRecyclerItemClickedInterFace onRecyclerItemClickedInterFace) {
        this.activity = activity;
        this.commonMessageDialog = commonMessageDialog;
        this.productList = productList;
        this.databaseMethods = databaseMethods;
        this.callFrom = callFrom;
        this.costValue = costValue;
        this.onRecyclerItemClickedInterFace = onRecyclerItemClickedInterFace;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_favorites, parent, false);
            return new ProductDataClass(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_layout_swipe, parent, false);
            return new ProgressClass(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductDataClass) {
            ProductDetailModel productDetailModel = productList.get(position);
            int count = databaseMethods.getAllProductQuantity(productDetailModel.getProduct_id());
            productDetailModel.setProduct_quantity(count);
            if (productDetailModel.getProduct_quantity() > 0) {
                ((ProductDataClass) holder).itemsAvailableInCart.setVisibility(View.VISIBLE);
                ((ProductDataClass) holder).blurImage.setVisibility(View.VISIBLE);
                ((ProductDataClass) holder).noItemsInCart.setVisibility(View.GONE);
                ((ProductDataClass) holder).noOfProductsTextView.setText(String.valueOf(productDetailModel.getProduct_quantity()));
            } else {
                ((ProductDataClass) holder).itemsAvailableInCart.setVisibility(View.GONE);
                ((ProductDataClass) holder).blurImage.setVisibility(View.INVISIBLE);
                ((ProductDataClass) holder).noItemsInCart.setVisibility(View.VISIBLE);
            }

            ((ProductDataClass) holder).productName.setText(productDetailModel.getProduct_name());

            ((ProductDataClass) holder).productPrice.setText(activity.getString(R.string.DOLLAR_SIGN)
                    .concat(" ")
                    .concat(String.valueOf(productDetailModel.getSale_price())));

            ((ProductDataClass) holder).noOfProductsTextView.setText(String.valueOf(productDetailModel.getProduct_quantity()));
            ((ProductDataClass) holder).productDescription.setText(productDetailModel.getDescription());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new RoundedCorners(30));
            Glide.with(activity)
                    .asBitmap()
                    .load(productDetailModel.getImage_url())
                    .apply(requestOptions)
                    .into(((ProductDataClass) holder).image);
        } else if (holder instanceof ProgressClass) {
            ((ProgressClass) holder).progressLayout.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public int getItemViewType(int position) {
        if (!productList.get(position).isLoadingType()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductDataClass extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.plus)
        ImageView plus;
        @BindView(R.id.minus)
        ImageView minus;
        @BindView(R.id.noOfProductsTextView)
        TextView noOfProductsTextView;
        @BindView(R.id.productName)
        TextView productName;
        @BindView(R.id.productPrice)
        TextView productPrice;
        @BindView(R.id.productDescription)
        TextView productDescription;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.mainLinear)
        LinearLayout mainLinear;
        @BindView(R.id.noItemsInCart)
        LinearLayout noItemsInCart;
        @BindView(R.id.itemsAvailableInCart)
        LinearLayout itemsAvailableInCart;
        @BindView(R.id.blurImage)
        ImageView blurImage;
        @BindView(R.id.crossCardView)
        CardView crossCardView;

        ProductDataClass(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (callFrom.equalsIgnoreCase(MyConstants.FAVORITE_ACTIVITY)) {
                crossCardView.setVisibility(View.VISIBLE);
            } else {
                crossCardView.setVisibility(View.GONE);
            }

            productName.setSelected(true);
            mainLinear.setOnClickListener(this);
            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
            noItemsInCart.setOnClickListener(this);
            crossCardView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            currentPosition = getAdapterPosition();
            if (currentPosition >= 0) {
                ProductDetailModel productDetailModel = productList.get(currentPosition);
                switch (v.getId()) {
                    case R.id.plus:
                    case R.id.noItemsInCart:
                        if (productDetailModel.getProduct_quantity() > 0) {
//                            boolean isProductExists = databaseMethods.productExistsInCart(productDetailModel.getProduct_id());
                            boolean finalResult = databaseMethods.addOrUpdate(productDetailModel, true);
                            CommonMethods.showLog(TAG, "final : " + finalResult);
                            if (finalResult) {
                                notifyItemChanged(currentPosition);
                                sendBroadcast();
//                                if (!isProductExists) {
//                                    onTrendingProductsClickedInterFace.onTrendingProductsClicked(currentPosition, MyConstants.PRODUCT_ADDED);
//                                }
                            }
                        } else {
                            Intent intent = new Intent(activity, ProductDetailActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra(MyConstants.PRODUCT_ID, productDetailModel.getProduct_id());
//                            intent.putExtra(MyConstants.PRODUCT_ADDITIONAL_FEATURES, "");

                            activity.startActivityForResult(intent, MyConstants.ADD_OR_UPDATE_CART);
                            activity.overridePendingTransition(0, 0);
                        }
                        break;
                    case R.id.minus:
                        if (productDetailModel.getProduct_quantity() > 0) {
                            CommonMethods.showLog(TAG, "If Works");
                            CommonMethods.showLog(TAG, "Count Before : " + productDetailModel.getProduct_quantity());
                            boolean result = databaseMethods.addOrUpdate(productDetailModel, false);
                            if (result) {
                                notifyItemChanged(currentPosition);
//                                int count = databaseMethods.getProductQuantity(productDetailModel.getProduct_id());
                                if (productDetailModel.getProduct_quantity() == 0) {
                                    if (callFrom.equalsIgnoreCase(MyConstants.CART)) {
                                        productList.remove(currentPosition);
                                        notifyItemRemoved(currentPosition);
                                    }
//                                    onRecyclerItemClickedInterFace.onRecyclerItemClicked(currentPosition);
                                }
                            }
                        } else {
                            if (callFrom.equalsIgnoreCase(MyConstants.CART)) {
                                productList.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                            }
                        }
                        if (activity instanceof MainActivity) {
                            ((MainActivity) activity).updateCartValue();
                        }
                        sendBroadcast();
                        onRecyclerItemClickedInterFace.onRecyclerItemClicked(currentPosition, "");
                        break;

                    case R.id.crossCardView:
                        databaseMethods.deleteParticularFavoriteProduct(productDetailModel.getProduct_id());
                        productList.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                        onRecyclerItemClickedInterFace.onRecyclerItemClicked(currentPosition, callFrom);
                        Intent intent = new Intent();
                        intent.putExtra(MyConstants.TYPE, MyConstants.UPDATE_FAVORITE);
                        intent.setAction(MyConstants.HOME_FRAGMENT_BROADCAST);
                        activity.sendBroadcast(intent);
                        sendBroadcast();
                        break;


                }
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

    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.putExtra(MyConstants.TYPE, MyConstants.UPDATE_CART_ITEMS);
        intent.setAction(MyConstants.MAIN_ACTIVITY_BROADCAST);
        activity.sendBroadcast(intent);
    }

}
