package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.AdditionalFeaturesRecyclerAdapter;
import com.napworks.steerlyfeapp.designsPackage.CustomProductInfoContainer;
import com.napworks.steerlyfeapp.designsPackage.CustomSellerContainer;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.fragmentsPackage.CartFragment;
import com.napworks.steerlyfeapp.interfacePackage.CustomerHomeAdapterInterface;
import com.napworks.steerlyfeapp.interfacePackage.ProductDetailResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.AdditionalFeaturesModel;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.modelPackage.ProductAvailabilityModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.ProductInfoModel;
import com.napworks.steerlyfeapp.modelPackage.SellerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends ParentActivity implements ProductDetailResponseInterface, CustomerHomeAdapterInterface, View.OnClickListener {

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.profileImage)
    ImageView profileImage;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.noOfReviews)
    TextView noOfReviews;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarText;
    @BindView(R.id.sellerText)
    TextView sellerText;
    //    @BindView(R.id.chooseSellerText)
//    TextView chooseSellerText;
    @BindView(R.id.browseCategoriesRecyclerView)
    RecyclerView browseCategoriesRecyclerView;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.inflatedProductInfoLayout)
    LinearLayout inflatedProductInfoLayout;
    @BindView(R.id.inflatedSellerLayout)
    LinearLayout inflatedSellerLayout;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.addToFavouritesView)
    RelativeLayout addToFavouritesView;
    @BindView(R.id.favouritesButton)
    TextView favouritesButton;

    private AdditionalFeaturesRecyclerAdapter additionalFeaturesRecyclerAdapter;
    private List<AdditionalFeaturesModel> additionalFeaturesList;
    private List<ProductAvailabilityModel> availabilityList;
    LoadingDialog loadingDialog;
    SharedPreferences sharedPreferences;
    CommonMessageDialog commonMessageDialog;
    private String productId = "";
    private String productName = "";
    private ProductDetailModel productDetailModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        CommonMethods.toolbarInitialize(this, toolbar, "", toolbarText);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        additionalFeaturesList = new ArrayList<>();
        availabilityList = new ArrayList<>();
        commonMessageDialog = new CommonMessageDialog(this);
        if (getIntent() != null) {
            productId = getIntent().getStringExtra(MyConstants.PRODUCT_ID);
            CommonMethods.showLog(TAG, "Product  Id : " + productId);
        }


        mainLinear.setVisibility(View.GONE);
        hitApi();

        browseCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        additionalFeaturesRecyclerAdapter = new AdditionalFeaturesRecyclerAdapter(this, additionalFeaturesList, this);
        browseCategoriesRecyclerView.setAdapter(additionalFeaturesRecyclerAdapter);


        favouritesButton.setOnClickListener(this);

    }

    /*
     * Product Detail Api
     */

    private void hitApi() {
        loadingDialog.showDialog();
        CommonWebServices.getProductDetail(sharedPreferences, productId, this);
    }


    /*
     * Product Detail Api Response
     */

    @Override
    public void getProductDetailResponse(String status, ProductDetailMainModel productDetailMainModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (productDetailMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Product Detail Api Status : " + productDetailMainModel.getStatus());
                    switch (productDetailMainModel.getStatus()) {
                        case "1":
                            mainLinear.setVisibility(View.VISIBLE);
                            CommonMethods.showLog(TAG, "Product Detail Api Success");
                            setData(productDetailMainModel.getProductDetail());
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Product Detail Status 2 : " + productDetailMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(productDetailMainModel.getMessage());
                            break;
                    }
                } else {
                    commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                }
                break;
            case MyConstants.NULL_RESPONSE:
            case MyConstants.FAILURE_RESPONSE:
                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                break;
        }
    }



    /*
     * Set Data
     */

    private void setData(ProductDetailModel productDetailModel) {
        this.productDetailModel = productDetailModel;

        List<ProductInfoModel> productInfoList = new ArrayList<>();

        if (productDetailModel.getAdditional_features().size() > 0) {
            CommonMethods.showLog(TAG, "ENTER ADDITIONAL FEATURES METHOD");
            for (int i = 0; i < productDetailModel.getAdditional_features().size(); i++) {
                AdditionalFeaturesModel innerModel = productDetailModel.getAdditional_features().get(i);
                additionalFeaturesList.add(new AdditionalFeaturesModel(innerModel.getValue(), innerModel.getPrice(), innerModel.getUnitType(), false, innerModel.getSellers()));
            }
            CommonMethods.showLog(TAG, "LIST SIZE : " + additionalFeaturesList.size());
            additionalFeaturesList.get(0).setSelected(true);
            additionalFeaturesRecyclerAdapter.notifyItemRangeInserted(0,additionalFeaturesList.size());
        }

        if (productDetailModel.getProduct_categories().size() > 0) {
            StringBuilder categoryValue = new StringBuilder();
            for (int i = 0; i < productDetailModel.getProduct_categories().size(); i++) {
                CategoryModel innerModel = productDetailModel.getProduct_categories().get(i);
                categoryValue.append(innerModel.getCategoryName());
                if (i < productDetailModel.getProduct_categories().size() - 1) {
                    categoryValue.append(", ");
                }
            }
            productInfoList.add(new ProductInfoModel(getString(R.string.category), categoryValue.toString()));
            CommonMethods.showLog(TAG, "Category Value : " + categoryValue.toString() + " P List Size : " + productInfoList.size());
        }


        if (productDetailModel.getProduct_info().size() > 0) {
            productInfoList.addAll(productDetailModel.getProduct_info());
        }

        inflatedProductInfoLayout.removeAllViews();
        if (productInfoList.size() > 0) {
            new CustomProductInfoContainer(this, productInfoList, inflatedProductInfoLayout);

        } else {
            CommonMethods.showLog(TAG, "No Info Available ");
        }

        productName = productDetailModel.getProduct_name();
        availabilityList.addAll(productDetailModel.getProduct_availability());

        addSellerView();


        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(20));
        Glide.with(this)
                .asBitmap()
                .load(productDetailModel.getImage_url())
                .apply(requestOptions)
                .into(profileImage);

        name.setText(productDetailModel.getProduct_name());
        rating.setRating(productDetailModel.getRating());
        noOfReviews.setText("( ".concat(String.valueOf(productDetailModel.getRating_count())).concat(" ").concat(getString(R.string.reviews).concat(" )")));

    }


    /*
     * Add Seller View
     */

    private void addSellerView() {
        List<SellerModel> sellerList = new ArrayList<>();
        inflatedSellerLayout.removeAllViews();
        CommonMethods.showLog(TAG, "ADDITIONAL FEATURES LIST : " + additionalFeaturesList.size());
        for (int i = 0; i < additionalFeaturesList.size(); i++) {
            AdditionalFeaturesModel innerModel = additionalFeaturesList.get(i);
            if (innerModel.isSelected()) {
                sellerList.addAll(innerModel.getSellers());
            }
        }
        if (sellerList.size() > 0) {
//            sellerText.setVisibility(View.GONE);
            CommonMethods.showLog(TAG, "If ");
            inflatedSellerLayout.setVisibility(View.VISIBLE);
            sellerText.setText(getString(R.string.chooseASeller));
            new CustomSellerContainer(getSupportFragmentManager(), this, additionalFeaturesList, inflatedSellerLayout, availabilityList, productDetailModel);
        } else {
            CommonMethods.showLog(TAG, "No Seller Available ");
            sellerText.setVisibility(View.VISIBLE);
            inflatedSellerLayout.setVisibility(View.GONE);
            sellerText.setText(getString(R.string.noSellerAvailable));
        }
    }

    public FragmentManager getActivityFragmentManager() {
        return getSupportFragmentManager();
    }


    @Override
    public void customerHomeAdapterInterface(String status, int position, String categoryId) {
        addSellerView();
    }


    /*
     * Added to Cart View
     */

    public void showViewAddedToCart(String callFrom, String type) {
        if (callFrom.equalsIgnoreCase(MyConstants.SELLER_FRAGMENT)) {
            addToFavouritesView.setVisibility(View.VISIBLE);
            if (type.equalsIgnoreCase(MyConstants.PRODUCT_ADDED)) {
                title.setText(getString(R.string.itemAddedToCart));
                favouritesButton.setText(getString(R.string.cart));
            } else {
                title.setText(getString(R.string.itemRemovedFromCart));
                favouritesButton.setText(getString(R.string.cart));
            }
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            addToFavouritesView.setAnimation(animation);
            animation.start();
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    addToFavouritesView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.favouritesButton) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(MyConstants.CALLED_FROM, MyConstants.PRODUCT_ACTIVITY);
            overridePendingTransition(0, 0);
            finishAffinity();
            startActivity(intent);
        }
    }
}
