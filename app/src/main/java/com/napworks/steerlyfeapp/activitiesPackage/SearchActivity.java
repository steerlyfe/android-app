package com.napworks.steerlyfeapp.activitiesPackage;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.FavoritesRecyclerAdapter;
import com.napworks.steerlyfeapp.adapterPackage.NearByStoresAdapter;
import com.napworks.steerlyfeapp.adapterPackage.SearchAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.OnCartItemClickedInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnRecyclerItemClickedInterFace;
import com.napworks.steerlyfeapp.interfacePackage.ProductCategoryResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.SearchResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.ProductCategoriesMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.SearchMainModel;
import com.napworks.steerlyfeapp.modelPackage.StoreDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchActivity extends ParentActivity implements TextView.OnEditorActionListener, OnRecyclerItemClickedInterFace, OnCartItemClickedInterface, View.OnClickListener, SearchResponseInterface {

    private String TAG = getClass().getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.storesRecyclerView)
    RecyclerView storesRecyclerView;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.cancelSearchButton)
    TextView cancelSearchButton;
    @BindView(R.id.productsText)
    TextView productsText;
    @BindView(R.id.storesText)
    TextView storesText;
    @BindView(R.id.storesMessageTextView)
    TextView storesMessageTextView;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.coordinateLayout)
    CoordinatorLayout coordinateLayout;
    @BindView(R.id.backArrow)
    ImageView backArrow;

    List<ProductDetailModel> productList;

    LoadingDialog loadingDialog;

    SharedPreferences sharedPreferences;
    CommonMessageDialog commonMessageDialog;
    LinearLayoutManager linearLayoutManager;
    DatabaseMethods databaseMethods;
    boolean last = true, retofitLoading = true, refresh = true;
    NearByStoresAdapter nearByStoresAdapter;
    private List<StoreDetailModel> storesList;
    private FavoritesRecyclerAdapter favoritesRecyclerAdapter;
    private double lat = 0.0, lng = 0.0;
    private FusedLocationProviderClient mFusedLocationClient;
    int locationRequestCode = 10;
    String latLngExists = "";
    MainBroadcastReceiver mainBroadcastReceiver;
    IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        coordinateLayout.scrollTo(0, 0);
        appbar.setExpanded(true);

        mainBroadcastReceiver = new MainBroadcastReceiver();
        intentFilter = new IntentFilter(MyConstants.PRODUCT_ACTIVITY_BROADCAST);
        messageTextView.setVisibility(View.GONE);
        productList = new ArrayList<>();
        storesList = new ArrayList<>();
        loadingDialog = new LoadingDialog(this);
        commonMessageDialog = new CommonMessageDialog(this);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        databaseMethods = new DatabaseMethods(this);
        edtSearch.requestFocus();
        edtSearch.setOnEditorActionListener(this);

        int width = CommonMethods.getWidth(this);
        width = (int) (width / 1.4);
        storesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nearByStoresAdapter = new NearByStoresAdapter(this, storesList, width, null);
        storesRecyclerView.setAdapter(nearByStoresAdapter);

        favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(this, productList, commonMessageDialog, databaseMethods,
                MyConstants.SEARCH, 0.0, this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(favoritesRecyclerAdapter);

        storesText.setVisibility(View.GONE);
        productsText.setVisibility(View.GONE);


        cancelSearchButton.setOnClickListener(this);
        backArrow.setOnClickListener(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkPermissionAndCallApis();


//        productList.clear();
//        scrollInitialize();
    }

    public void checkPermissionAndCallApis() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        } else {
            if (CommonMethods.checkLocationOnOff(this)) {
                CommonMethods.showLog(TAG, "location enabled");
                getLocation();
            } else {
                CommonMethods.showLog(TAG, "location not enabled");
                latLngExists = "0";
            }
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    latLngExists = "1";

                    CommonMethods.showLog(TAG, "getLocation  not null");
                    CommonMethods.showLog(TAG, "getLocation lat " + lat);
                    CommonMethods.showLog(TAG, "getLocation lng " + lng);
                } else {
                    latLngExists = "0";
                }
            }
        });
    }
    /*
     * Search Api
     */
    private void hitApi() {
//        retofitLoading = true;
//        int count = 0;
//        if (!refresh) {
//            if (productList.size() > 0) {
//                count = productList.size() - 1;
//            }
//        }

        loadingDialog.showDialog();
        CommonWebServices.searchApi(sharedPreferences,
                edtSearch.getText().toString().trim(),
                0,
                latLngExists,
                String.valueOf(lat),
                String.valueOf(lng),
                this);
    }

//    @Override
//    public void onRefresh() {
//        refresh = true;
//        last = true;
//        hitApi();
//    }


    private void scrollInitialize() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int total = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisible + 1 == total && !retofitLoading) {
                    if (last) {
                        insertFakeEntry();
                        hitApi();

                    }
                }
            }
        });
    }

    private void insertFakeEntry() {
        CommonMethods.showLog(TAG, "Chlda" + productList.size());
        productList.add(new ProductDetailModel("", "", "", "", "",
                0.0, 0.0, 0, " ", "", 0.0, "", 0.0, "", "", "", true));
        favoritesRecyclerAdapter.notifyItemInserted(productList.size() - 1);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getText().length() < 1) {
            commonMessageDialog.showDialog(getString(R.string.enterValidKeyword));
        } else {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hitApi();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(edtSearch.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        return false;
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
                favoritesRecyclerAdapter.notifyItemChanged(position);


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

//    @Override
//    public void getProductCategoryResponse(String status, ProductCategoriesMainModel productCategoriesMainModel) {
//        switch (status) {
//            case MyConstants.GO_TO_RESULT:
//                if (productCategoriesMainModel.getStatus() != null) {
//                    CommonMethods.showLog(TAG, "Product Category Api Status : " + productCategoriesMainModel.getStatus());
//                    switch (productCategoriesMainModel.getStatus()) {
//                        case "1":
////                            if (refresh) {
////                                cartRecyclerAdapter.notifyItemRangeRemoved(0, productList.size());
////                                productList.clear();
////                                refresh = false;
////                            }
////                            if (productList.size() > 0) {
////                                productList.remove(productList.size() - 1);
////                                cartRecyclerAdapter.notifyItemRemoved(productList.size());
////                            }
//
//                            productList.addAll(productCategoriesMainModel.getProductList());
//                            cartRecyclerAdapter.notifyDataSetChanged();
////                            cartRecyclerAdapter.notifyItemRangeChanged(productList.size() - productCategoriesMainModel.getProductList().size(),
////                                    productCategoriesMainModel.getProductList().size());
////                            if (productCategoriesMainModel.getProductList().size() < productCategoriesMainModel.getPerPageItems()) {
////                                CommonMethods.showLog(TAG, "last chal gya");
////                                last = false;
////                            } else {
////                                last = true;
////                            }
//                            checkAndShow();
//                            break;
//
//                        case "2":
//                            CommonMethods.showLog(TAG, "Product Category Status 2 : " + productCategoriesMainModel.getMessage());
//                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
//                            break;
//
//                        case "11":
//                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
//                            break;
//                        case "10":
//                            CommonMethods.sessionOut(this);
//                            break;
//                        case "0":
//                            commonMessageDialog.showDialog(productCategoriesMainModel.getMessage());
//                            break;
//                    }
//                    retofitLoading = false;
//                } else {
//                    commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
//                }
//                break;
//            case MyConstants.NULL_RESPONSE:
//            case MyConstants.FAILURE_RESPONSE:
//                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
//                break;
//        }
//    }

    private void checkAndShow() {
        if (productList.size() == 0) {
            messageTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            messageTextView.setText(getResources().getString(R.string.noProductAvailableTextSearchActivity));
        } else {
            messageTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        }
        if (storesList.size() == 0) {
            storesMessageTextView.setVisibility(View.VISIBLE);
            storesMessageTextView.setText(getResources().getString(R.string.noStoreAvailableText));
        } else {
            storesRecyclerView.setVisibility(View.VISIBLE);
            storesMessageTextView.setVisibility(View.GONE);

        }


    }

    @Override
    public void onRecyclerItemClicked(int position, String callFrom) {

    }

    @Override
    public void onCartItemClicked(List<ProductDetailModel> productList, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrow:
                onBackPressed();
                break;
            case R.id.cancelSearchButton:
                edtSearch.setText("");
                break;
        }
    }
    /*
     * Search Api Response
     */
    @Override
    public void getSearchResponse(String status, SearchMainModel searchMainModel) {
        loadingDialog.hideDialog();
        storesText.setVisibility(View.VISIBLE);
        productsText.setVisibility(View.VISIBLE);
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (searchMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Search Api Status : " + searchMainModel.getStatus());
                    switch (searchMainModel.getStatus()) {
                        case "1":
                            if (productList.size() > 0) {
                                favoritesRecyclerAdapter.notifyItemRangeRemoved(0, productList.size());
                            }

                            if (storesList.size() > 0) {
                                nearByStoresAdapter.notifyItemRangeRemoved(0, productList.size());
                            }
                            productList.clear();
                            storesList.clear();
                            productList.addAll(searchMainModel.getProductList());
                            favoritesRecyclerAdapter.notifyDataSetChanged();
                            storesList.addAll(searchMainModel.getStoreList());
                            nearByStoresAdapter.notifyDataSetChanged();
                            checkAndShow();
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Search Status 2 : " + searchMainModel.getErrorData());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(searchMainModel.getMessage());
                            break;
                    }
                    retofitLoading = false;
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
}
