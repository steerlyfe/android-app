package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.BuyOrdersAgainAdapter;
import com.napworks.steerlyfeapp.adapterPackage.DealsStoreDetailAdapter;
import com.napworks.steerlyfeapp.adapterPackage.OrdersRecyclerAdapter;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.interfacePackage.OrderHistoryResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.OrderHistoryInnerModel;
import com.napworks.steerlyfeapp.modelPackage.OrderHistoryMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersActivity extends ParentActivity implements SwipeRefreshLayout.OnRefreshListener, OrderHistoryResponseInterface, View.OnClickListener {

    private String TAG = getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.buyOrderRecyclerView)
    RecyclerView buyOrderRecyclerView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarText;
    @BindView(R.id.topLayout)
    RelativeLayout topLayout;
    @BindView(R.id.viewAll)
    LinearLayout viewAll;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.coordinateLayout)
    CoordinatorLayout coordinateLayout;

    boolean last = true, retofitLoading = true, refresh = true;
    LinearLayoutManager linearLayoutManager;

    private SharedPreferences sharedPreferences;
    private CommonMessageDialog commonMessageDialog;
    private OrdersRecyclerAdapter ordersRecyclerAdapter;
    private List<OrderHistoryInnerModel> ordersList;
    private String orderId = "";

    BuyOrdersAgainAdapter buyOrdersAgainAdapter;
    private List<ProductDetailModel> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(this);
        ordersList = new ArrayList<>();
        productList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        commonMessageDialog = new CommonMessageDialog(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.orderHistory), toolbarText);
        coordinateLayout.scrollTo(0, 0);
        appbar.setExpanded(true);

        ordersRecyclerAdapter = new OrdersRecyclerAdapter(this, ordersList, commonMessageDialog);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ordersRecyclerAdapter);

        int screenWidth = CommonMethods.getWidth(this);
        int density = (int) CommonMethods.getScreenDensity(this);
        screenWidth = (int) (screenWidth / 2.5);
        screenWidth = screenWidth - (density * 30);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        buyOrdersAgainAdapter = new BuyOrdersAgainAdapter(productList, this, screenWidth);
        buyOrderRecyclerView.setLayoutManager(linearLayoutManager);
        buyOrderRecyclerView.setAdapter(buyOrdersAgainAdapter);

        swipeContainer.setOnRefreshListener(this);
        ordersList.clear();
        swipeContainer.setRefreshing(true);
        topLayout.setVisibility(View.GONE);


        viewAll.setOnClickListener(this);

        hitApi();
        scrollInitialize();

    }

    /*
     * Order History Api
     */

    private void hitApi() {
        retofitLoading = true;
        int count = 0;
        if (!refresh) {
            if (ordersList.size() > 0) {
                count = ordersList.size() - 1;
            }
        }


        CommonMethods.showLog(TAG, "Category Id : " + orderId);
        CommonWebServices.getOrderHistory(sharedPreferences,
                count,
                this);


    }

    @Override
    public void onRefresh() {
        productList.clear();
        buyOrdersAgainAdapter.notifyItemRangeRemoved(0, productList.size());
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
        CommonMethods.showLog(TAG, "Chlda" + ordersList.size());
        ordersList.add(new OrderHistoryInnerModel("", "", 0.0, 0.0, 0.0, 0.0,
                false, "", 0.0, 0.0, 0,
                null, "", true, null));
        ordersRecyclerAdapter.notifyItemInserted(ordersList.size() - 1);
    }

    /*
     * Order History  Api Response
     */

    @Override
    public void getOrderHistoryResponse(String status, OrderHistoryMainModel orderHistoryMainModel) {
        topLayout.setVisibility(View.VISIBLE);
        swipeContainer.setRefreshing(false);
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (orderHistoryMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Product Category Api Status : " + orderHistoryMainModel.getStatus());
                    switch (orderHistoryMainModel.getStatus()) {
                        case "1":
                            if (refresh) {
                                ordersRecyclerAdapter.notifyItemRangeRemoved(0, ordersList.size());
                                ordersList.clear();
                                refresh = false;
                            }
                            if (ordersList.size() > 0) {
                                ordersList.remove(ordersList.size() - 1);
                                ordersRecyclerAdapter.notifyItemRemoved(ordersList.size());
                            }


                            productList.addAll(orderHistoryMainModel.getProductList());
                            buyOrdersAgainAdapter.notifyDataSetChanged();
                            ordersList.addAll(orderHistoryMainModel.getOrderList());
                            ordersRecyclerAdapter.notifyItemRangeChanged(ordersList.size() - orderHistoryMainModel.getOrderList().size(),
                                    orderHistoryMainModel.getOrderList().size());
                            if (orderHistoryMainModel.getOrderList().size() < orderHistoryMainModel.getPerPageItems()) {
                                CommonMethods.showLog(TAG, "last chal gya");
                                last = false;
                            } else {
                                last = true;
                            }
                            checkAndShow();
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Product Category Status 2 : " + orderHistoryMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(orderHistoryMainModel.getMessage());
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
        if (ordersList.size() == 0) {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(getResources().getString(R.string.noOrderAvailableYet));
        } else {
            messageTextView.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewAll:
                Intent intent = new Intent(this, ViewOrderedProductsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.ORDER_DETAIL) {
            if (resultCode == RESULT_OK) {
                topLayout.setVisibility(View.GONE);
                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        }
    }
}
