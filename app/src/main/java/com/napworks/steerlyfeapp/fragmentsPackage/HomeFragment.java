package com.napworks.steerlyfeapp.fragmentsPackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.FavoritesActivity;
import com.napworks.steerlyfeapp.activitiesPackage.NearByStoresActivity;
import com.napworks.steerlyfeapp.activitiesPackage.QrCodeScanner;
import com.napworks.steerlyfeapp.activitiesPackage.SearchActivity;
import com.napworks.steerlyfeapp.adapterPackage.SuggestedProductsAdapter;
import com.napworks.steerlyfeapp.adapterPackage.TrendingProductsAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.HomeDataResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnTrendingProductsClickedInterFace;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.HomeDataMainModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements HomeDataResponseInterface, View.OnClickListener, OnTrendingProductsClickedInterFace {

    private DatabaseMethods databaseMethods;

    public HomeFragment() {
    }

    private final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R.id.trendingProductsRecycler)
    RecyclerView trendingProductsRecycler;
    @BindView(R.id.suggestedProductsRecycler)
    RecyclerView suggestedProductsRecycler;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.favouritesLinear)
    LinearLayout favouritesLinear;
    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;
    @BindView(R.id.addToFavouritesView)
    RelativeLayout addToFavouritesView;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.homeMessage)
    TextView homeMessage;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.favouritesButton)
    TextView favouritesButton;
    @BindView(R.id.nearByStores)
    ImageView nearByStores;
    @BindView(R.id.barcode)
    ImageView barcode;
    private SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    private CommonMessageDialog commonMessageDialog;

    private TrendingProductsAdapter trendingProductsAdapter;
    private SuggestedProductsAdapter suggestedProductsAdapter;

    private List<ProductDetailModel> trendingProductList = new ArrayList<>();
    private List<ProductDetailModel> suggestedProductList = new ArrayList<>();
    MainBroadcastReceiver mainBroadcastReceiver;
    IntentFilter intentFilter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        trendingProductList = new ArrayList<>();
        suggestedProductList = new ArrayList<>();
        databaseMethods = new DatabaseMethods(getActivity());
        mainLinear.setVisibility(View.GONE);
//        setHasOptionsMenu(true);
        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
            loadingDialog = new LoadingDialog(getActivity());
            commonMessageDialog = new CommonMessageDialog(getActivity());
            mainBroadcastReceiver = new MainBroadcastReceiver();
            intentFilter = new IntentFilter(MyConstants.HOME_FRAGMENT_BROADCAST);
            int screenWidth = CommonMethods.getWidth(getActivity());
            int density = (int) CommonMethods.getScreenDensity(getActivity());
            int screenWidthForSuggestedAdapter = (int) (screenWidth / 2.5);
            screenWidthForSuggestedAdapter = screenWidthForSuggestedAdapter - (density * 30);
            CommonMethods.showLog(TAG, "Density : " + density);
            CommonMethods.showLog(TAG, "Screen Width : " + screenWidthForSuggestedAdapter);
            screenWidth = screenWidth / 2;
            screenWidth = screenWidth - (density * 30);

            setNameText();


            hitApi();

            LinearLayoutManager linearTrendingProductsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            trendingProductsAdapter = new TrendingProductsAdapter(trendingProductList, getActivity(), screenWidth, databaseMethods, this);
            trendingProductsRecycler.setLayoutManager(linearTrendingProductsLayoutManager);
            trendingProductsRecycler.setAdapter(trendingProductsAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            suggestedProductsAdapter = new SuggestedProductsAdapter(suggestedProductList, getActivity(), screenWidthForSuggestedAdapter);
            suggestedProductsRecycler.setLayoutManager(linearLayoutManager);
            suggestedProductsRecycler.setAdapter(suggestedProductsAdapter);

            searchLayout.setOnClickListener(this);
            barcode.setOnClickListener(this);
            nearByStores.setOnClickListener(this);
            favouritesLinear.setOnClickListener(this);
            favouritesButton.setOnClickListener(this);
        }
        return view;
    }

    private void setNameText() {
        String wishText = "";
        int hour = Integer.parseInt(CommonMethods.getHourOfDay(System.currentTimeMillis()));
        CommonMethods.showLog(TAG, "Hour : " + hour);
        if (hour >= 6 && hour < 12) {
            CommonMethods.showLog(TAG, "good Morning");
            wishText = getString(R.string.good_morning);
        } else if (hour == 12) {
            CommonMethods.showLog(TAG, "good noon");
            wishText = getString(R.string.good_noon);
        } else if (hour >= 13 && hour < 17) {
            CommonMethods.showLog(TAG, "good afternoon");
            wishText = getString(R.string.good_afternoon);
        } else if (hour >= 17 && hour < 22) {
            CommonMethods.showLog(TAG, "good evening");
            wishText = getString(R.string.good_evening);
        } else {
            CommonMethods.showLog(TAG, "good night");
            wishText = getString(R.string.good_night);
        }
        String name = sharedPreferences.getString(MyConstants.FULL_NAME, "");
        if (name != null) {
            nameText.setText(wishText.concat(" ").concat(name));
        } else {
            nameText.setText(wishText);
        }
    }

    private void hitApi() {
        CommonWebServices.getHomeData(sharedPreferences, this);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.home_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.barcode:
//                CommonMethods.showLog(TAG, "Barcode Clicked");
//                break;
//            case R.id.location:
//                CommonMethods.showLog(TAG, "Location Clicked");
//                if (getActivity() != null) {
//                    Intent intent = new Intent(getActivity(), NearByStoresActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    intent.putExtra(MyConstants.SUB_STORE_ID, "2");
//                    getActivity().startActivity(intent);
//                }
//                break;
//
//        }
//
//        return true;
//    }

    @Override
    public void getHomeDataResponse(String status, HomeDataMainModel homeDataMainModel) {
        if (getActivity() != null) {
            switch (status) {
                case MyConstants.GO_TO_RESULT:
                    if (homeDataMainModel.getStatus() != null) {
                        CommonMethods.showLog(TAG, "Home Data Api Status : " + homeDataMainModel.getStatus());
                        switch (homeDataMainModel.getStatus()) {
                            case "1":
                                mainLinear.setVisibility(View.VISIBLE);
                                CommonMethods.showLog(TAG, "Home Data Api Success");

                                homeMessage.setText(homeDataMainModel.getHomeMessage());
                                trendingProductList.clear();
                                trendingProductList.addAll(homeDataMainModel.getTrendingProducts());
                                trendingProductsAdapter.notifyItemRangeInserted(0, trendingProductList.size());


                                suggestedProductList.clear();
                                suggestedProductList.addAll(homeDataMainModel.getSuggestedProducts());
                                suggestedProductsAdapter.notifyItemRangeInserted(0, suggestedProductList.size());

//                                checkAndShow();


                                break;

                            case "2":
                                CommonMethods.showLog(TAG, "Update Data Status 2 : " + homeDataMainModel.getMessage());
                                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                                break;

                            case "11":
                                commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                                break;
                            case "10":
                                CommonMethods.sessionOut(getActivity());
                                break;
                            case "0":
                                commonMessageDialog.showDialog(homeDataMainModel.getMessage());
                                break;
                        }
                    } else {
                        commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                    }
                    break;
                case MyConstants.NULL_RESPONSE:
                case MyConstants.FAILURE_RESPONSE:
                    commonMessageDialog.showDialog(getActivity().getString(R.string.some_errors_occurred_text));
                    break;
            }
        }


    }

//    private void checkAndShow() {
//
//        if (suggestedProductList.size()==0)
//        {
//
//        }
//        else
//        {
//
//        }
//
//
//    }

    @Override
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.searchLayout:
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().startActivity(intent);
                    break;
                case R.id.barcode:
                    CommonMethods.showLog(TAG, "Barcode Clicked");
                    intent = new Intent(getActivity(), QrCodeScanner.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().startActivity(intent);
                    break;
                case R.id.nearByStores:
                    CommonMethods.showLog(TAG, "NearByStores Clicked");
                    intent = new Intent(getActivity(), NearByStoresActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra(MyConstants.SUB_STORE_ID, "2");
                    getActivity().startActivity(intent);
                    break;
                case R.id.favouritesLinear:
                case R.id.favouritesButton:
                    CommonMethods.showLog(TAG, "favourites Linear Clicked");
                    intent = new Intent(getActivity(), FavoritesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onTrendingProductsClicked(int position, String type) {
        showItemsAddedView(type);
    }

    private void showItemsAddedView(String type) {
        if (getActivity() != null) {
            addToFavouritesView.setVisibility(View.VISIBLE);
            if (type.equalsIgnoreCase(MyConstants.PRODUCT_ADDED)) {
                title.setText(getActivity().getString(R.string.itemAddedToFavourites));
                favouritesButton.setText(getActivity().getString(R.string.favourites));
            } else {
                title.setText(getActivity().getString(R.string.itemRemovedFromFavourites));
                favouritesButton.setText(getActivity().getString(R.string.favourites));
            }
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
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
    public void onDestroy() {
        if (getActivity() != null) {
            super.onDestroy();
            getActivity().unregisterReceiver(mainBroadcastReceiver);
        }
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            super.onResume();
            getActivity().registerReceiver(mainBroadcastReceiver, intentFilter);
        }
    }

    private class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(MyConstants.TYPE);
            if (type.equalsIgnoreCase(MyConstants.UPDATE_FAVORITE)) {
                CommonMethods.showLog(TAG, "Update Favorite Broadcast Hit");
                suggestedProductsAdapter.notifyItemRangeRemoved(0, suggestedProductList.size());
                trendingProductsAdapter.notifyItemRangeRemoved(0, trendingProductList.size());
                hitApi();
            }
        }
    }
}
