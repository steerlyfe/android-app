package com.napworks.steerlyfeapp.activitiesPackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.FavoritesRecyclerAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.interfacePackage.OnRecyclerItemClickedInterFace;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends ParentActivity implements OnRecyclerItemClickedInterFace {

    private String TAG = getClass().getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarTextView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.addToFavouritesView)
    RelativeLayout addToFavouritesView;
    @BindView(R.id.favouritesButton)
    TextView favouritesButton;

    private FavoritesRecyclerAdapter cartRecyclerAdapter;
    private List<ProductDetailModel> productList;
    private CommonMessageDialog commonMessageDialog;
    DatabaseMethods databaseMethods;

    MainBroadcastReceiver mainBroadcastReceiver;
    IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.favorite_products), toolbarTextView);
        databaseMethods = new DatabaseMethods(this);
        commonMessageDialog = new CommonMessageDialog(this);
        productList = new ArrayList<>();
        mainBroadcastReceiver = new MainBroadcastReceiver();
        intentFilter = new IntentFilter(MyConstants.MAIN_ACTIVITY_BROADCAST);
        addDataIntoList();


    }
    /*
     * Add Favorites Products into list
     */
    private void addDataIntoList() {
        List<ProductDetailModel> list = databaseMethods.getAllFavoriteProductList();
        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getProduct_quantity() > 0) {
            ProductDetailModel innerModel = list.get(i);
            CommonMethods.showLog(TAG, "If");
            CommonMethods.showLog(TAG, "getProduct_id : " + innerModel.getProduct_id());
            CommonMethods.showLog(TAG, "getAdditionalFeaturesType : " + innerModel.getAdditionalFeaturesType());
            CommonMethods.showLog(TAG, "getSub_store_id : " + innerModel.getSub_store_id());
            CommonMethods.showLog(TAG, "getProductAvailabilityType : " + innerModel.getProductAvailabilityType());

            productList.add(new ProductDetailModel(innerModel.getProduct_id(), innerModel.getDescription(), innerModel.getCategory_id(),
                    innerModel.getProduct_name(), innerModel.getStore_id(), innerModel.getActual_price(), innerModel.getSale_price(),
                    innerModel.getProduct_quantity(), innerModel.getImage_url(), false));
            CommonMethods.showLog(TAG, "Product List Size : " + productList.size());

//            } else {
//                CommonMethods.showLog(TAG, "Else");
//                databaseMethods.deleteParticularProduct(list.get(i).getProduct_id());
//            }
        }


        int count = databaseMethods.getAllFavouriteProductCount();

        CommonMethods.showLog(TAG, "Favorite Product List Size : " + productList.size());
        CommonMethods.showLog(TAG, "Favorite Product Count : " + count);
        cartRecyclerAdapter = new FavoritesRecyclerAdapter(this, productList, commonMessageDialog, databaseMethods, MyConstants.FAVORITE_ACTIVITY, 0.0, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cartRecyclerAdapter);

        checkAndShow();

    }

    private void checkAndShow() {
        if (productList.size() == 0) {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(getString(R.string.noProductAvailableInFavoriteText));

        } else {
            messageTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRecyclerItemClicked(int position, String callFrom) {
        if (callFrom.equalsIgnoreCase(MyConstants.FAVORITE_ACTIVITY)) {
            showItemsAddedView(MyConstants.PRODUCT_REMOVED_FROM_FAV);
        }
        checkAndShow();
    }
    /*
     * For Items Added View
     */
    private void showItemsAddedView(String type) {
        addToFavouritesView.setVisibility(View.VISIBLE);
        if (type.equalsIgnoreCase(MyConstants.PRODUCT_ADDED)) {
            title.setText(getString(R.string.itemAddedToCart));
            favouritesButton.setText(getString(R.string.cart));
        } else if (type.equalsIgnoreCase(MyConstants.PRODUCT_REMOVED)) {
            title.setText(getString(R.string.itemRemovedFromCart));
            favouritesButton.setText(getString(R.string.cart));
        } else if (type.equalsIgnoreCase(MyConstants.PRODUCT_ADDED_TO_FAV)) {
            title.setText(getString(R.string.itemAddedToFavourites));
            favouritesButton.setText(getString(R.string.favourites));
        } else {
            title.setText(getString(R.string.itemRemovedFromFavourites));
            favouritesButton.setText(getString(R.string.favourites));
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

    public void showAddedRemovedView(String type) {
        showItemsAddedView(type);
    }

    private class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(MyConstants.TYPE);
            if (type.equalsIgnoreCase(MyConstants.UPDATE_CART_ITEMS)) {
                CommonMethods.showLog(TAG, "Broadcast Hit");

                cartRecyclerAdapter.notifyItemRangeRemoved(0, productList.size());
                productList.clear();
                addDataIntoList();

            }
        }
    }

}
