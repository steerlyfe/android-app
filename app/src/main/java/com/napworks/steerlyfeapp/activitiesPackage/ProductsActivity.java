package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.CategoriesRecyclerAdapter;
import com.napworks.steerlyfeapp.adapterPackage.ProductRecyclerAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.FilterCustomerDialog;
import com.napworks.steerlyfeapp.interfacePackage.CustomerHomeAdapterInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnTrendingProductsClickedInterFace;
import com.napworks.steerlyfeapp.interfacePackage.ProductCategoryResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.modelPackage.ProductCategoriesMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.SortingModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductsActivity extends ParentActivity implements ProductCategoryResponseInterface, SwipeRefreshLayout.OnRefreshListener,
        CustomerHomeAdapterInterface, View.OnClickListener, OnTrendingProductsClickedInterFace
//        , View.OnClickListener
{

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.browseCategoriesRecyclerView)
    RecyclerView browseCategoriesRecyclerView;
    //    @BindView(R.id.checkOutButton)
//    TextView checkOutButton;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.noOfItems)
    TextView noOfItems;
    @BindView(R.id.searchText)
    TextView searchText;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.pageTitle)
    TextView pageTitle;
    @BindView(R.id.sortingText)
    TextView sortingText;
    @BindView(R.id.favouritesButton)
    TextView favouritesButton;
    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.bottomLayout)
    LinearLayout bottomLayout;
    @BindView(R.id.sortLayout)
    LinearLayout sortLayout;
    @BindView(R.id.addToFavouritesView)
    RelativeLayout addToFavouritesView;

    private SharedPreferences sharedPreferences;
    private CommonMessageDialog commonMessageDialog;
    private ProductRecyclerAdapter productRecyclerAdapter;
    private CategoriesRecyclerAdapter categoriesRecyclerAdapter;
    private List<ProductDetailModel> productList;
    private String categoryId = "";
    boolean last = true, retofitLoading = true, refresh = true;
    GridLayoutManager gridLayoutManager;
    DatabaseMethods databaseMethods;

    private List<SortingModel> sortingList;
    private List<CategoryModel> categoriesList;
    private List<CategoryModel> databaseCategoriesList;
    String categoryName = "", sortValue = "Top Rated ";
    FilterCustomerDialog filterCustomerDialog;
    MainBroadcastReceiver mainBroadcastReceiver;
    IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
        productList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        commonMessageDialog = new CommonMessageDialog(this);
        databaseMethods = new DatabaseMethods(this);
        mainBroadcastReceiver = new MainBroadcastReceiver();
        intentFilter = new IntentFilter(MyConstants.PRODUCT_ACTIVITY_BROADCAST);
        categoriesList = new ArrayList<>();
        databaseCategoriesList = new ArrayList<>();
        sortingList = new ArrayList<>();
        filterCustomerDialog = new FilterCustomerDialog(this);


        addDataIntoSortingList();
        searchText.setSelected(true);
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra(MyConstants.CATEGORY_ID);
            categoryName = getIntent().getStringExtra(MyConstants.CATEGORY_NAME);
            pageTitle.setText(categoryName);
        }

        int screenWidth = CommonMethods.getWidth(this) / 2;
        int density = (int) CommonMethods.getScreenDensity(this);
        screenWidth = screenWidth - (density * 10);


        databaseCategoriesList.addAll(databaseMethods.getAllCategoriesList());
        CommonMethods.showLog(TAG, "Databse Cat List : " + databaseCategoriesList.size());


        for (int i = 0; i < databaseCategoriesList.size(); i++) {
            CategoryModel innerModel = databaseCategoriesList.get(i);
            if (innerModel.getCategoryName().equalsIgnoreCase(categoryName)) {
                categoriesList.add(new CategoryModel(innerModel.getCategoryId(), innerModel.getCategoryName(), innerModel.getCategoryUrl(), true));
            } else {
                categoriesList.add(new CategoryModel(innerModel.getCategoryId(), innerModel.getCategoryName(), innerModel.getCategoryUrl(), false));
            }
        }

        sortingText.setText(sortValue);

        CommonMethods.showLog(TAG, "Databse Cat List : " + categoriesList.size());
        browseCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerAdapter = new CategoriesRecyclerAdapter(this, categoriesList, this);
        browseCategoriesRecyclerView.setAdapter(categoriesRecyclerAdapter);

        swipeContainer.setOnRefreshListener(this);
        productRecyclerAdapter = new ProductRecyclerAdapter(this, productList, commonMessageDialog, databaseMethods, "", screenWidth, this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(productRecyclerAdapter);


        productList.clear();
        swipeContainer.setRefreshing(true);

        hitApi();
        scrollInitialize();

        sortLayout.setOnClickListener(this);
        backArrow.setOnClickListener(this);


    }


    /*
     * Add Data Into Sorting List
     */


    private void addDataIntoSortingList() {
        sortingList.add(new SortingModel(getString(R.string.topRated), true, MyConstants.SORTING_TOP_RATED));
        sortingList.add(new SortingModel(getString(R.string.priceLowToHigh), false, MyConstants.SORTING_PRICE_LOW_TO_HIGH));
        sortingList.add(new SortingModel(getString(R.string.priceHighToLow), false, MyConstants.SORTING_PRICE_HIGH_TO_LOW));
        sortingList.add(new SortingModel(getString(R.string.mostPopular), false, MyConstants.SORTING_MOST_POPULAR));
        sortingList.add(new SortingModel(getString(R.string.newestArrivals), false, MyConstants.SORTING_NEWEST_ARRIVAL));
        sortingList.add(new SortingModel(getString(R.string.featured), false, MyConstants.SORTING_FEATURED));
    }


    /*
     * Category Products Api
     */

    private void hitApi() {
        retofitLoading = true;
        int count = 0;
        if (!refresh) {
            if (productList.size() > 0) {
                count = productList.size() - 1;
            }
        }


        CommonMethods.showLog(TAG, "Category Id : " + categoryId);
        CommonWebServices.getCategoryProducts(sharedPreferences,
                categoryId,
                count,
                sortValue(),
                this);


    }


    /*
     * Method of sort Value
     */
    private String sortValue() {
        String value = "";
        for (int i = 0; i < sortingList.size(); i++) {
            if (sortingList.get(i).isSelected) {
                value = sortingList.get(i).getSortingValue();
                sortValue = sortingList.get(i).getTitle();
            }
        }
        return value;
    }

    @Override
    public void onRefresh() {
        refresh = true;
        last = true;
        hitApi();
    }


    private void scrollInitialize() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int total = gridLayoutManager.getItemCount();
                int lastVisible = gridLayoutManager.findLastVisibleItemPosition();
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
                0.0, 0.0, 0, " ", "", 0.0,
                "", 0.0, "", "", "", true));
        productRecyclerAdapter.notifyItemInserted(productList.size() - 1);
    }


    /*
     * Get Product Category Api
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
                            if (refresh) {
                                productRecyclerAdapter.notifyItemRangeRemoved(0, productList.size());
                                productList.clear();
                                refresh = false;
                            }
                            if (productList.size() > 0) {
                                productList.remove(productList.size() - 1);
                                productRecyclerAdapter.notifyItemRemoved(productList.size());
                            }

//                            databaseMethods.deleteAllProducts();

                            noOfItems.setText(String.valueOf(productCategoriesMainModel.getTotalProducts()).concat(" ").concat(getString(R.string.items)));

                            productList.addAll(productCategoriesMainModel.getProductList());
                            productRecyclerAdapter.notifyItemRangeChanged(productList.size() - productCategoriesMainModel.getProductList().size(),
                                    productCategoriesMainModel.getProductList().size());
                            if (productCategoriesMainModel.getProductList().size() < productCategoriesMainModel.getPerPageItems()) {
                                CommonMethods.showLog(TAG, "last chal gya");
                                last = false;
                            } else {
                                last = true;
                            }
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

    private void checkAndShow() {
        if (productList.size() == 0) {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(getResources().getString(R.string.noProductAvailableText));
        } else {
            messageTextView.setVisibility(View.GONE);

        }
    }

    @Override
    public void customerHomeAdapterInterface(String status, int position, String categoryId) {
        switch (status) {
            case "1":
                this.categoryId = categoryId;
                pageTitle.setText(categoriesList.get(position).getCategoryName());
                CommonMethods.showLog(TAG, "CategoryId : " + categoryId);
                productRecyclerAdapter.notifyItemRangeRemoved(0, productList.size());
                productList.clear();
                swipeContainer.setRefreshing(true);
                hitApi();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sortLayout:
                filterCustomerDialog.showDialog(sortingList);
                break;
            case R.id.backArrow:
                onBackPressed();
                break;
        }

    }


    /*
     * Method to update sort data
     */

    public void updateSortingData(List<SortingModel> sortingList, int position) {
        this.sortingList = sortingList;
        sortingText.setText(sortingList.get(position).getTitle());
        productRecyclerAdapter.notifyItemRangeRemoved(0, productList.size());
        productList.clear();
        swipeContainer.setRefreshing(true);
        hitApi();
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
}
