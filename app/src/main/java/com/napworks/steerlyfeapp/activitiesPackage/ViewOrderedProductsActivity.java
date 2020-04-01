package com.napworks.steerlyfeapp.activitiesPackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.CategoriesRecyclerAdapter;
import com.napworks.steerlyfeapp.adapterPackage.ProductRecyclerAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.FilterCustomerDialog;
import com.napworks.steerlyfeapp.interfacePackage.CustomerHomeAdapterInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnTrendingProductsClickedInterFace;
import com.napworks.steerlyfeapp.interfacePackage.ProductCategoryResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.ProductDetailResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.modelPackage.ProductCategoriesMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.SortingModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ViewOrderedProductsActivity extends ParentActivity implements
        SwipeRefreshLayout.OnRefreshListener, OnTrendingProductsClickedInterFace, ProductCategoryResponseInterface
//        , View.OnClickListener
{

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarText;
    @BindView(R.id.addToFavouritesView)
    RelativeLayout addToFavouritesView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.favouritesButton)
    TextView favouritesButton;

    private SharedPreferences sharedPreferences;
    private CommonMessageDialog commonMessageDialog;
    private ProductRecyclerAdapter productRecyclerAdapter;
    private List<ProductDetailModel> productList;
    GridLayoutManager gridLayoutManager;
    DatabaseMethods databaseMethods;

    MainBroadcastReceiver mainBroadcastReceiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ordered_products);
        ButterKnife.bind(this);
        productList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        commonMessageDialog = new CommonMessageDialog(this);
        databaseMethods = new DatabaseMethods(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.products), toolbarText);

        mainBroadcastReceiver = new MainBroadcastReceiver();
        intentFilter = new IntentFilter(MyConstants.PRODUCT_ACTIVITY_BROADCAST);

        int screenWidth = CommonMethods.getWidth(this) / 2;
        int density = (int) CommonMethods.getScreenDensity(this);
        screenWidth = screenWidth - (density * 10);


        swipeContainer.setOnRefreshListener(this);
        productRecyclerAdapter = new ProductRecyclerAdapter(this, productList, commonMessageDialog, databaseMethods,
                MyConstants.VIEW_PREVIOUS_ORDERED_PRODUCTS, screenWidth, this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(productRecyclerAdapter);

        productList.clear();
        swipeContainer.setRefreshing(true);
        hitApi();

    }
    /*
     * Previous Order Products  Api
     */
    private void hitApi() {
        CommonWebServices.getAllPreviousOrderedProducts(sharedPreferences,
                this);
    }

    @Override
    public void onRefresh() {
        productRecyclerAdapter.notifyItemRangeRemoved(0, productList.size());
        productList.clear();
        swipeContainer.setRefreshing(true);
        hitApi();
    }
    /*
     * Previous Order Products  Api Response
     */
    @Override
    public void getProductCategoryResponse(String status, ProductCategoriesMainModel productCategoriesMainModel) {
        swipeContainer.setRefreshing(false);
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (productCategoriesMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Product Category Api Status : " + productCategoriesMainModel.getStatus());
                    switch (productCategoriesMainModel.getStatus()) {
                        case "1":
                            productList.addAll(productCategoriesMainModel.getProductList());
                            productRecyclerAdapter.notifyDataSetChanged();
                            checkAndShow();
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Product Category Status 2 : " + productCategoriesMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(productCategoriesMainModel.getMessage());
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

    private void checkAndShow() {
        if (productList.size() == 0) {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(getResources().getString(R.string.noProductAvailableText));
        } else {
            messageTextView.setVisibility(View.GONE);

        }
    }

    @Override
    public void onTrendingProductsClicked(int position, String type) {
        addToFavouritesView.setVisibility(View.VISIBLE);
        if (type.equalsIgnoreCase(MyConstants.PRODUCT_ADDED)) {
            title.setText(getString(R.string.itemAddedToCart));
            favouritesButton.setText(getString(R.string.cart));
        } else if (type.equalsIgnoreCase(MyConstants.PRODUCT_ADDED_TO_FAV)) {
            title.setText(getString(R.string.itemAddedToFavourites));
            favouritesButton.setText(getString(R.string.favourites));
        } else if (type.equalsIgnoreCase(MyConstants.PRODUCT_REMOVED_FROM_FAV)) {
            title.setText(getString(R.string.itemRemovedFromFavourites));
            favouritesButton.setText(getString(R.string.favourites));
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

    private class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = 0;

            String type = intent.getStringExtra(MyConstants.TYPE);
            if (type.equalsIgnoreCase(MyConstants.UPDATE_CART_ITEMS)) {
                CommonMethods.showLog(TAG, "Broadcast Hit");

                ProductDetailModel productDetailModel = (ProductDetailModel) intent.getSerializableExtra(MyConstants.PRODUCT_DETAIL_MODEL);
                for (int i = 0; i < productList.size(); i++) {
                    if (productDetailModel.getProduct_id().equalsIgnoreCase(productList.get(i).getProduct_id())) {
                        CommonMethods.showLog(TAG, "Broadcast Hit , Match : " + i);
                        productList.get(i).setProduct_quantity(productDetailModel.getProduct_quantity());
                        position = i;
                    }
                }
                productRecyclerAdapter.notifyItemChanged(position);


//                productRecyclerAdapter.notifyItemRangeRemoved(0, productList.size());
//                productList.clear();
//                hitApi();


            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mainBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mainBroadcastReceiver, intentFilter);
    }

}
