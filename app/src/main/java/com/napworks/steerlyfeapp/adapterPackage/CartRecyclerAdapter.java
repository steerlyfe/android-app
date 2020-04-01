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
import com.napworks.steerlyfeapp.activitiesPackage.ProductDetailActivity;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.interfacePackage.CheckCouponAmountInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnCartItemClickedInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnRecyclerItemClickedInterFace;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final DatabaseMethods databaseMethods;
    private final String callFrom;
    private final OnRecyclerItemClickedInterFace onRecyclerItemClickedInterFace;
    private final OnCartItemClickedInterface onCartItemClickedInterface;
    private final CheckCouponAmountInterface checkCouponAmountInterface;
    private CommonMessageDialog commonMessageDialog;
    private Activity activity;
    private List<ProductDetailModel> productList;
    private String TAG = getClass().getSimpleName();
    private int currentPosition = 0;
    double costValue = 0.0;

    public CartRecyclerAdapter(Activity activity, List<ProductDetailModel> productList,
                               CommonMessageDialog commonMessageDialog, DatabaseMethods databaseMethods, String callFrom,
                               double costValue,
                               OnRecyclerItemClickedInterFace onRecyclerItemClickedInterFace,
                               OnCartItemClickedInterface onCartItemClickedInterface,
                               CheckCouponAmountInterface checkCouponAmountInterface) {
        this.activity = activity;
        this.commonMessageDialog = commonMessageDialog;
        this.productList = productList;
        this.databaseMethods = databaseMethods;
        this.callFrom = callFrom;
        this.costValue = costValue;
        this.onRecyclerItemClickedInterFace = onRecyclerItemClickedInterFace;
        this.checkCouponAmountInterface = checkCouponAmountInterface;
        this.onCartItemClickedInterface = onCartItemClickedInterface;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_cart, parent, false);
            return new ProductDataClass(view);
        } else if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cost, parent, false);
            return new CostClass(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_layout_swipe, parent, false);
            return new ProgressClass(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductDetailModel productDetailModel = productList.get(position);
        if (holder instanceof ProductDataClass) {
            int productQuantity = 0;

            if (callFrom.equalsIgnoreCase(MyConstants.CART)) {

                productQuantity = databaseMethods.getProductQuantityUpdated(productDetailModel.getProduct_id(),
                        productDetailModel.getAdditionalFeaturesType(),
                        productDetailModel.getSub_store_id(), productDetailModel.getProductAvailabilityType());
                productDetailModel.setProduct_quantity(productQuantity);
            } else {
                productQuantity = databaseMethods.getProductQuantity(productDetailModel.getProduct_id());
                productDetailModel.setProduct_quantity(productQuantity);
            }

//            double price = productDetailModel.getSale_price() + productDetailModel.getAdditionalFeaturesPrice();
//            costValue = costValue + (price * productQuantity) + productDetailModel.getProductAvailabilityPrice();

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

            ((ProductDataClass) holder).productName.setText(productDetailModel.getProduct_name().
                    concat(" (").
                    concat(productDetailModel.getAdditionalFeaturesType()).
                    concat(")"));

            if (productDetailModel.getProductAvailabilityType().equalsIgnoreCase(MyConstants.DELIVER_NOW)) {
                ((ProductDataClass) holder).productPrice.setText(activity.getString(R.string.DOLLAR_SIGN)
                        .concat(" ")
                        .concat(String.valueOf(productDetailModel.getSale_price() + productDetailModel.getAdditionalFeaturesPrice()))
                        .concat(" + Delivery : ")
                        .concat(activity.getString(R.string.DOLLAR_SIGN))
                        .concat(" ")
                        .concat(String.valueOf(productDetailModel.getProductAvailabilityPrice())));

            } else if (productDetailModel.getProductAvailabilityType().equalsIgnoreCase(MyConstants.SHIPPING)) {
                ((ProductDataClass) holder).productPrice.setText(activity.getString(R.string.DOLLAR_SIGN)
                        .concat(" ")
                        .concat(String.valueOf(productDetailModel.getSale_price() + productDetailModel.getAdditionalFeaturesPrice()))
                        .concat(" + Shipping : ")
                        .concat(activity.getString(R.string.DOLLAR_SIGN))
                        .concat(" ")
                        .concat(String.valueOf(productDetailModel.getProductAvailabilityPrice())));

            } else {
                ((ProductDataClass) holder).productPrice.setText(activity.getString(R.string.DOLLAR_SIGN)
                        .concat(" ")
                        .concat(String.valueOf(productDetailModel.getSale_price() + productDetailModel.getAdditionalFeaturesPrice())));
            }


            ((ProductDataClass) holder).noOfProductsTextView.setText(String.valueOf(productDetailModel.getProduct_quantity()));
            ((ProductDataClass) holder).productDescription.setText(productDetailModel.getDescription());
            ((ProductDataClass) holder).sellerName.setText(productDetailModel.getSeller_name());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new RoundedCorners(30));
            Glide.with(activity)
                    .asBitmap()
                    .load(productDetailModel.getImage_url())
                    .apply(requestOptions)
                    .into(((ProductDataClass) holder).image);
        } else if (holder instanceof CostClass) {
            CommonMethods.showLog(TAG, "Cost is : " + costValue);
            costValue = productList.get(position).getActual_price();
            if (productList.size() == 1) {
                ((CostClass) holder).costLinear.setVisibility(View.GONE);

            } else {
                ((CostClass) holder).costLinear.setVisibility(View.VISIBLE);
                ((CostClass) holder).totalCost.setText("$".concat(String.valueOf(CommonMethods.roundOff(costValue))));

                if (productDetailModel.getAdditionalFeaturesPrice() == 0) {
                    ((CostClass) holder).couponDiscountLayout.setVisibility(View.GONE);
                    ((CostClass) holder).orderCostLayout.setVisibility(View.GONE);
                    ((CostClass) holder).couponAppliedLayout.setVisibility(View.GONE);
//                    ((CostClass) holder).cancel.setVisibility(View.GONE);
                    ((CostClass) holder).totalCostLayout.setVisibility(View.VISIBLE);
                } else {
                    ((CostClass) holder).couponDiscountLayout.setVisibility(View.VISIBLE);
                    ((CostClass) holder).orderCostLayout.setVisibility(View.VISIBLE);
                    ((CostClass) holder).totalCostLayout.setVisibility(View.VISIBLE);
                    ((CostClass) holder).couponAppliedLayout.setVisibility(View.VISIBLE);
//                    ((CostClass) holder).cancel.setVisibility(View.VISIBLE);

                    ((CostClass) holder).orderCost.setText("$".concat(String.valueOf(CommonMethods.roundOff(productDetailModel.getActual_price()))));
                    ((CostClass) holder).totalCost.setText("$".concat(String.valueOf(CommonMethods.roundOff(productDetailModel.getSale_price()))));
                    ((CostClass) holder).couponCode.setText(productDetailModel.getProduct_name());
                    ((CostClass) holder).couponDiscount.setText("- ".concat(" $").concat(String.valueOf(CommonMethods.roundOff(productDetailModel.getAdditionalFeaturesPrice()))));
                }
            }
        } else if (holder instanceof ProgressClass) {
            ((ProgressClass) holder).progressLayout.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (callFrom.equalsIgnoreCase(MyConstants.CART)) {
//            CommonMethods.showLog(TAG, "Call from Cart Activity");
            if (!productList.get(position).isLoadingType()) {
                viewType = 1;
            } else {
                viewType = 2;
            }
        } else {
//            CommonMethods.showLog(TAG, "Call from Other Activity");
            if (!productList.get(position).isLoadingType()) {
                viewType = 1;
            } else {
                viewType = 3;
            }
        }
        return viewType;
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
        @BindView(R.id.sellerName)
        TextView sellerName;
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
                        if (callFrom.equalsIgnoreCase(MyConstants.CART)) {
                            boolean finalResult = databaseMethods.addOrUpdateProductsUpdated(productDetailModel, true);
                            CommonMethods.showLog(TAG, "final : " + finalResult);
                            if (finalResult) {
                                notifyItemChanged(currentPosition);
                                sendBroadcast();
                            }
                            costValue = costValue + productDetailModel.getSale_price() +
                                    productDetailModel.getProductAvailabilityPrice() +
                                    productDetailModel.getAdditionalFeaturesPrice();
                            productList.get(productList.size() - 1).setActual_price(CommonMethods.roundOff(costValue));
                            notifyItemChanged(productList.size() - 1);

                            checkCouponAmountInterface.getCheckCouponAmountResponse(MyConstants.CHECK_COUPON_AMOUNT,currentPosition);


                        } else {
                            if (productDetailModel.getProduct_quantity() > 0) {
                                boolean finalResult = databaseMethods.addOrUpdate(productDetailModel, true);
                                CommonMethods.showLog(TAG, "final : " + finalResult);
                                if (finalResult) {
                                    notifyItemChanged(currentPosition);
                                    sendBroadcast();
                                }
                            } else {
                                Intent intent = new Intent(activity, ProductDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra(MyConstants.PRODUCT_ID, productDetailModel.getProduct_id());
//                                intent.putExtra(MyConstants.PRODUCT_ADDITIONAL_FEATURES, "");

                                activity.startActivityForResult(intent, MyConstants.ADD_OR_UPDATE_CART);
                                activity.overridePendingTransition(0, 0);
                            }
                        }

                        break;
                    case R.id.minus:
                        if (productDetailModel.getProduct_quantity() > 0) {
                            CommonMethods.showLog(TAG, "If Works");
                            CommonMethods.showLog(TAG, "Count Before : " + productDetailModel.getProduct_quantity());
                            boolean result = databaseMethods.addOrUpdateProductsUpdated(productDetailModel, false);
                            if (result) {
                                notifyItemChanged(currentPosition);
                                if (callFrom.equalsIgnoreCase(MyConstants.CART)) {
                                    if (productDetailModel.getProduct_quantity() == 1) {
                                        productList.remove(currentPosition);
                                        notifyItemRemoved(currentPosition);
                                        costValue = costValue - (productDetailModel.getSale_price() +
                                                productDetailModel.getProductAvailabilityPrice() +
                                                productDetailModel.getAdditionalFeaturesPrice());
                                        productList.get(productList.size() - 1).setActual_price(CommonMethods.roundOff(costValue));
                                        notifyItemChanged(productList.size() - 1);
                                        onCartItemClickedInterface.onCartItemClicked(productList, currentPosition);
                                    } else {
                                        costValue = costValue - (productDetailModel.getSale_price() +
                                                productDetailModel.getAdditionalFeaturesPrice());
                                        productList.get(productList.size() - 1).setActual_price(CommonMethods.roundOff(costValue));
                                        notifyItemChanged(productList.size() - 1);
                                    }


//                                        productList.remove(currentPosition);
//                                        notifyItemRemoved(currentPosition);
                                }
                            }
                        } else {
                            if (callFrom.equalsIgnoreCase(MyConstants.CART)) {
                                productList.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                costValue = costValue - (productDetailModel.getSale_price() +
                                        productDetailModel.getProductAvailabilityPrice() +
                                        productDetailModel.getAdditionalFeaturesPrice());
                                productList.get(productList.size() - 1).setActual_price(CommonMethods.roundOff(costValue));
                                notifyItemChanged(productList.size() - 1);
                                onCartItemClickedInterface.onCartItemClicked(productList, currentPosition);

                            }
                        }
                        checkCouponAmountInterface.getCheckCouponAmountResponse(MyConstants.CHECK_COUPON_AMOUNT,currentPosition);
                        sendBroadcast();
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

    class CostClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.totalCost)
        TextView totalCost;
        @BindView(R.id.orderCost)
        TextView orderCost;
        @BindView(R.id.couponDiscount)
        TextView couponDiscount;
        //        @BindView(R.id.cancel)
//        TextView cancel;
        @BindView(R.id.couponCode)
        TextView couponCode;
        @BindView(R.id.costLinear)
        LinearLayout costLinear;
        @BindView(R.id.couponDiscountLayout)
        LinearLayout couponDiscountLayout;
        @BindView(R.id.totalCostLayout)
        LinearLayout totalCostLayout;
        @BindView(R.id.orderCostLayout)
        LinearLayout orderCostLayout;
        @BindView(R.id.couponAppliedLayout)
        LinearLayout couponAppliedLayout;

        CostClass(View view) {
            super(view);
            ButterKnife.bind(this, view);
//            cancel.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cancel) {
                productList.get(getAdapterPosition()).setAdditionalFeaturesPrice(0.0);
                couponDiscountLayout.setVisibility(View.GONE);
//                cancel.setVisibility(View.GONE);
                orderCostLayout.setVisibility(View.GONE);
                notifyItemChanged(getAdapterPosition());
                onRecyclerItemClickedInterFace.onRecyclerItemClicked(getAdapterPosition(), MyConstants.CANCEL_COUPON);
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
