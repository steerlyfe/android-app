package com.napworks.steerlyfeapp.fragmentsPackage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.BarcodeProductDetailActivity;
import com.napworks.steerlyfeapp.activitiesPackage.ProductDetailActivity;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.modelPackage.ProductAvailabilityModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.SellerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SellerFragment extends Fragment implements View.OnClickListener {

    private int position;
    private int fragmentPosition;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.ratingTextView)
    TextView ratingTextView;
    @BindView(R.id.addToCartButton)
    TextView addToCartButton;
    @BindView(R.id.deliveryPrice)
    TextView deliveryPrice;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.plus)
    ImageView plus;
    @BindView(R.id.minus)
    ImageView minus;
    @BindView(R.id.noOfProductsTextView)
    TextView noOfProductsTextView;
    @BindView(R.id.noItemsInCart)
    LinearLayout noItemsInCart;
    @BindView(R.id.itemsAvailableInCart)
    RelativeLayout itemsAvailableInCart;
    @BindView(R.id.blurImage)
    ImageView blurImage;

    private ProductDetailModel productDetailModel;
    private ProductAvailabilityModel productAvailabilityModel;
    private Activity activity;
    private SellerModel sellerModel;
    DatabaseMethods databaseMethods;

    private String TAG = getClass().getSimpleName();

    public SellerFragment() {
    }

    public SellerFragment(Activity activity, SellerModel sellerModel, ProductAvailabilityModel productAvailabilityModel,
                          int position, int fragmentPosition, ProductDetailModel productDetailModel) {
        this.activity = activity;
        this.sellerModel = sellerModel;
        this.productAvailabilityModel = productAvailabilityModel;
        this.position = position;
        this.fragmentPosition = fragmentPosition;
        this.productDetailModel = productDetailModel;
        CommonMethods.showLog(TAG, "POSITION : " + position + " FRAG POS : " + fragmentPosition);
    }


//    public SellerFragment(Activity activity, List<SellerModel> sellerList, ProductAvailabilityModel productAvailabilityModel) {
//        this.activity = activity;
//        this.sellerList = sellerList;
//        this.productAvailabilityModel = productAvailabilityModel;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller, container, false);
        ButterKnife.bind(this, view);
        CommonMethods.showLog(TAG, "Seller Fragment : " + sellerModel.getAddress() + " Type : " + productAvailabilityModel.getType());
        CommonMethods.showLog(TAG, "POSITION : " + position + " FRAG POS : " + fragmentPosition);

        if (getActivity() != null) {
            databaseMethods = new DatabaseMethods(getActivity());
            if (productAvailabilityModel.isAvailable()) {
                messageTextView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                double price = sellerModel.getSelectedUnitPrice() + productDetailModel.getSale_price();

                amount.setText("$ ".concat(" ").concat(String.valueOf(price)));
                address.setText(sellerModel.getAddress());
                ratingTextView.setText(String.valueOf(sellerModel.getRating()));

                boolean isProductExists = databaseMethods.productExistsInCartUpdated(productDetailModel.getProduct_id(),
                        sellerModel.getSelectedUnit(),
                        sellerModel.getSub_store_id(), productAvailabilityModel.getType());
                CommonMethods.showLog(TAG, "Product Exists : " + isProductExists);

                if (isProductExists) {
                    int count = databaseMethods.getProductQuantityUpdated(productDetailModel.getProduct_id(),
                            sellerModel.getSelectedUnit(),
                            sellerModel.getSub_store_id(), productAvailabilityModel.getType());
//                    int count = databaseMethods.getProductQuantity(productDetailModel.getProduct_id());
                    itemsAvailableInCart.setVisibility(View.VISIBLE);
                    blurImage.setVisibility(View.VISIBLE);
                    noItemsInCart.setVisibility(View.GONE);
                    noOfProductsTextView.setText(String.valueOf(count));
                } else {
                    itemsAvailableInCart.setVisibility(View.GONE);
                    blurImage.setVisibility(View.INVISIBLE);
                    noItemsInCart.setVisibility(View.VISIBLE);
                }

                if (productAvailabilityModel.getType().equalsIgnoreCase(MyConstants.DELIVER_NOW)) {
                    deliveryPrice.setVisibility(View.VISIBLE);
                    deliveryPrice.setText(getActivity().getString(R.string.deliveryCharges).concat("  $").concat(String.valueOf(productAvailabilityModel.getPrice())));
                } else if (productAvailabilityModel.getType().equalsIgnoreCase(MyConstants.SHIPPING)) {
                    deliveryPrice.setVisibility(View.VISIBLE);
                    deliveryPrice.setText(getActivity().getString(R.string.shippingCharges).concat("  $").concat(String.valueOf(productAvailabilityModel.getPrice())));
                } else {
                    deliveryPrice.setVisibility(View.INVISIBLE);
                }
            } else {
                String availabilityType = "";
                linearLayout.setVisibility(View.INVISIBLE);
                messageTextView.setVisibility(View.VISIBLE);
                if (productAvailabilityModel.getType().equalsIgnoreCase(MyConstants.DELIVER_NOW)) {
                    availabilityType = getActivity().getString(R.string.delivery);
                } else if (productAvailabilityModel.getType().equalsIgnoreCase(MyConstants.IN_STORE)) {
                    availabilityType = getActivity().getString(R.string.inStoreText);
                } else {
                    availabilityType = getActivity().getString(R.string.shippingText);
                }

                messageTextView.setText(getActivity().getString(R.string.itSeemsLike).
                        concat(" ").
                        concat(productDetailModel.getProduct_name()).
                        concat(" ").
                        concat(getActivity().getString(R.string.isNotAvailableFor)).
                        concat(" ").
                        concat(availabilityType).
                        concat("."));
            }

        }
        noItemsInCart.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus:
            case R.id.noItemsInCart:
                CommonMethods.showLog(TAG, "Clicked : " + position + " A : " + fragmentPosition);
                productDetailModel.setAdditionalFeaturesType(sellerModel.getSelectedUnit());
                productDetailModel.setAdditionalFeaturesPrice(sellerModel.getSelectedUnitPrice());
                productDetailModel.setProductAvailabilityType(productAvailabilityModel.getType());
                productDetailModel.setProductAvailabilityPrice(productAvailabilityModel.getPrice());
                productDetailModel.setSub_store_id(sellerModel.getSub_store_id());
                productDetailModel.setStore_id(sellerModel.getStore_id());
                productDetailModel.setSeller_name(sellerModel.getName());
                productDetailModel.setSeller_address(sellerModel.getAddress());
                CommonMethods.showLog(TAG, "getType : " + productDetailModel.getProductAvailabilityType());
                CommonMethods.showLog(TAG, "getSelectedUnit : " + productDetailModel.getAdditionalFeaturesType());
                CommonMethods.showLog(TAG, "getSub_store_id : " + productDetailModel.getSub_store_id());
                CommonMethods.showLog(TAG, "getProduct_name : " + productDetailModel.getProduct_name());
                CommonMethods.showLog(TAG, "getSale_price : " + productDetailModel.getSale_price());
                CommonMethods.showLog(TAG, "getDescription : " + productDetailModel.getDescription());
                CommonMethods.showLog(TAG, "getStore_id : " + productDetailModel.getStore_id());

                boolean isProductExists = databaseMethods.productExistsInCartUpdated(productDetailModel.getProduct_id(),
                        productDetailModel.getAdditionalFeaturesType(),
                        sellerModel.getSub_store_id(), productAvailabilityModel.getType());
                boolean finalResult = databaseMethods.addOrUpdateProductsUpdated(productDetailModel, true);
                CommonMethods.showLog(TAG, "final : " + finalResult + " Position : " + position);
                if (finalResult) {
                    sendBroadcast();
                    if (!isProductExists) {
                        CommonMethods.showLog(TAG, "Product Added");
                        if (activity instanceof ProductDetailActivity) {
                            ((ProductDetailActivity) activity).showViewAddedToCart(MyConstants.SELLER_FRAGMENT, MyConstants.PRODUCT_ADDED);
                        }
//                        else {
//                            ((BarcodeProductDetailActivity) activity).showViewAddedToCart(MyConstants.SELLER_FRAGMENT, MyConstants.PRODUCT_ADDED);
//                        }
//                    onTrendingProductsClickedInterFace.onTrendingProductsClicked(currentPosition, MyConstants.PRODUCT_ADDED);
                    }

                    int count = databaseMethods.getProductQuantityUpdated(productDetailModel.getProduct_id(),
                            sellerModel.getSelectedUnit(),
                            sellerModel.getSub_store_id(), productAvailabilityModel.getType());

                    productDetailModel.setProduct_quantity(count);

                    if (count > 0) {
                        itemsAvailableInCart.setVisibility(View.VISIBLE);
                        blurImage.setVisibility(View.VISIBLE);
                        noItemsInCart.setVisibility(View.GONE);
                        noOfProductsTextView.setText(String.valueOf(count));
                    } else {
                        itemsAvailableInCart.setVisibility(View.GONE);
                        blurImage.setVisibility(View.INVISIBLE);
                        noItemsInCart.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.minus:


                productDetailModel.setAdditionalFeaturesType(sellerModel.getSelectedUnit());
                productDetailModel.setAdditionalFeaturesPrice(sellerModel.getSelectedUnitPrice());
                productDetailModel.setProductAvailabilityType(productAvailabilityModel.getType());
                productDetailModel.setProductAvailabilityPrice(productAvailabilityModel.getPrice());
                productDetailModel.setSub_store_id(sellerModel.getSub_store_id());
                productDetailModel.setStore_id(sellerModel.getStore_id());
                productDetailModel.setSeller_name(sellerModel.getName());
                productDetailModel.setSeller_address(sellerModel.getAddress());

                boolean finalResult1 = databaseMethods.addOrUpdateProductsUpdated(productDetailModel, false);
                CommonMethods.showLog(TAG, "final : " + finalResult1 + " Position : " + position);
                if (finalResult1) {
                    sendBroadcast();
//                    if (!isProductExists) {
//                        CommonMethods.showLog(TAG, "Product Added");
////                    onTrendingProductsClickedInterFace.onTrendingProductsClicked(currentPosition, MyConstants.PRODUCT_ADDED);
//                    }

                    int count = databaseMethods.getProductQuantityUpdated(productDetailModel.getProduct_id(),
                            sellerModel.getSelectedUnit(),
                            sellerModel.getSub_store_id(), productAvailabilityModel.getType());
                    productDetailModel.setProduct_quantity(count);

                    if (count > 0) {
                        itemsAvailableInCart.setVisibility(View.VISIBLE);
                        blurImage.setVisibility(View.VISIBLE);
                        noItemsInCart.setVisibility(View.GONE);
                        noOfProductsTextView.setText(String.valueOf(count));
                    } else {
                        itemsAvailableInCart.setVisibility(View.GONE);
                        blurImage.setVisibility(View.INVISIBLE);
                        noItemsInCart.setVisibility(View.VISIBLE);
                        if (activity instanceof ProductDetailActivity) {
                            ((ProductDetailActivity) activity).showViewAddedToCart(MyConstants.SELLER_FRAGMENT, MyConstants.PRODUCT_REMOVED);
                        }
                    }
                }
                break;
        }
    }

    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.putExtra(MyConstants.TYPE, MyConstants.UPDATE_CART_ITEMS);
        intent.setAction(MyConstants.MAIN_ACTIVITY_BROADCAST);
        activity.sendBroadcast(intent);

        intent = new Intent();
        intent.putExtra(MyConstants.TYPE, MyConstants.UPDATE_CART_ITEMS);
        intent.putExtra(MyConstants.PRODUCT_DETAIL_MODEL, productDetailModel);
        intent.setAction(MyConstants.PRODUCT_ACTIVITY_BROADCAST);
        activity.sendBroadcast(intent);

    }
}
