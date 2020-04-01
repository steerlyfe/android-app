package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.designsPackage.CustomViewPager;
import com.napworks.steerlyfeapp.fragmentsPackage.CartFragment;
import com.napworks.steerlyfeapp.fragmentsPackage.HomeFragment;
import com.napworks.steerlyfeapp.fragmentsPackage.LifestyleFragmentUpdated;
import com.napworks.steerlyfeapp.fragmentsPackage.ProfileFragment;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.fragmentsPackage.SearchFragment;
import com.napworks.steerlyfeapp.adapterPackage.ViewPagerAdapter;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.home_relative)
    RelativeLayout home_relative;
    @BindView(R.id.search_relative)
    RelativeLayout search_relative;
    @BindView(R.id.lifestyle_relative)
    RelativeLayout lifestyle_relative;
    @BindView(R.id.profile_relative)
    RelativeLayout profile_relative;
    @BindView(R.id.cart_relative)
    RelativeLayout cart_relative;
    @BindView(R.id.home_image)
    ImageView home_image;
    @BindView(R.id.search_image)
    ImageView search_image;
    @BindView(R.id.lifestyle_image)
    ImageView lifestyle_image;
    @BindView(R.id.profile_image)
    ImageView profile_image;
    @BindView(R.id.cart_image)
    ImageView cart_image;
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;
    @BindView(R.id.linearBottom)
    LinearLayout linearBottom;
    //    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.toolbarTextView)
//    TextView toolbarTextView;
//    @BindView(R.id.cartValue)
//    TextView cartValue;
    @BindView(R.id.homeTitle)
    TextView homeTitle;
    @BindView(R.id.searchTitle)
    TextView searchTitle;
    @BindView(R.id.lifestyleTitle)
    TextView lifestyleTitle;
    @BindView(R.id.accountTitle)
    TextView accountTitle;
    @BindView(R.id.cartTitle)
    TextView cartTitle;
    @BindView(R.id.noOfCartItems)
    TextView noOfCartItems;

    SharedPreferences sharedPreferences;
    ViewPagerAdapter adapter;
    DatabaseMethods databaseMethods;

    MainBroadcastReceiver mainBroadcastReceiver;
    IntentFilter intentFilter;
    private String callFrom = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);





        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        mainBroadcastReceiver = new MainBroadcastReceiver();
        intentFilter = new IntentFilter(MyConstants.MAIN_ACTIVITY_BROADCAST);
        CommonMethods.showLog(TAG, "USER ID : " + sharedPreferences.getString(MyConstants.USER_ID, ""));
        CommonMethods.showLog(TAG, "Auth : " + sharedPreferences.getString(MyConstants.AUTHORIZATION, ""));
        CommonMethods.showLog(TAG, "Session : " + sharedPreferences.getString(MyConstants.SESSION, ""));
        setupViewPager(viewpager);
//        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        databaseMethods = new DatabaseMethods(this);
        updateCartValue();

        viewpager.disableScroll(true);
        viewpager.addOnPageChangeListener(this);
        viewpager.setOffscreenPageLimit(1);

        if (getIntent() != null) {
            callFrom = getIntent().getStringExtra(MyConstants.CALLED_FROM);
        }

        if (callFrom.equalsIgnoreCase(MyConstants.PRODUCT_ACTIVITY))
        {
            changeBottomLayoutStyle(4);
            viewpager.setCurrentItem(4);
        }
        else
        {
            changeBottomLayoutStyle(0);
        }

        home_relative.setOnClickListener(this);
        search_relative.setOnClickListener(this);
        lifestyle_relative.setOnClickListener(this);
        profile_relative.setOnClickListener(this);
        cart_relative.setOnClickListener(this);

    }
    /*
     * View Pager
     */
    private void setupViewPager(ViewPager viewpager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0, this);
        adapter.addFrag(new HomeFragment(), "");
        adapter.addFrag(new SearchFragment(), "");
        adapter.addFrag(new LifestyleFragmentUpdated(), "");
        adapter.addFrag(new ProfileFragment(), "");
        adapter.addFrag(new CartFragment(this), "");
        viewpager.setAdapter(adapter);
    }

    public void updateStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeBottomLayoutStyle(position);
    }


    /*
     * Change Bottom Layout Style
     */

    private void changeBottomLayoutStyle(int position) {
        if (position == 0) {
//            cartValue.setVisibility(View.GONE);
//            toolbarTextView.setText(getString(R.string.home));
            CommonMethods.textGradient(homeTitle, this);
            CommonMethods.textGradient(searchTitle, this);
            CommonMethods.textGradient(lifestyleTitle, this);
            CommonMethods.textGradient(accountTitle, this);
            CommonMethods.textGradient(cartTitle, this);
            home_image.setImageResource(R.drawable.home_icon_filled);
            home_relative.setBackground(getResources().getDrawable(R.drawable.upper_left_round_selected_tab_background));
            search_image.setImageResource(R.drawable.explore_icon);
            search_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            lifestyle_image.setImageResource(R.drawable.life_icon);
            lifestyle_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            profile_image.setImageResource(R.drawable.user_icon);
            profile_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            cart_image.setImageResource(R.drawable.cart_icon);
            cart_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
        } else if (position == 1) {
//            cartValue.setVisibility(View.GONE);
//            toolbarTextView.setText(getString(R.string.explore));
            CommonMethods.textGradient(searchTitle, this);

            homeTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
            search_image.setImageResource(R.drawable.explore_icon_filled);
            search_relative.setBackgroundColor(getResources().getColor(R.color.selectedTabBackground));
            home_image.setImageResource(R.drawable.home_icon);
            home_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            lifestyle_image.setImageResource(R.drawable.life_icon);
            lifestyle_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            profile_image.setImageResource(R.drawable.user_icon);
            profile_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            cart_image.setImageResource(R.drawable.cart_icon);
            cart_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
        } else if (position == 2) {
//            cartValue.setVisibility(View.GONE);
//            toolbarTextView.setText(getString(R.string.lifeStyle));
            CommonMethods.textGradient(lifestyleTitle, this);
            lifestyle_image.setImageResource(R.drawable.life_icon_filled);
            lifestyle_relative.setBackgroundColor(getResources().getColor(R.color.selectedTabBackground));
            home_image.setImageResource(R.drawable.home_icon);
            home_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            search_image.setImageResource(R.drawable.explore_icon);
            search_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            profile_image.setImageResource(R.drawable.user_icon);
            profile_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            cart_image.setImageResource(R.drawable.cart_icon);
            cart_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
        } else if (position == 3) {
//            cartValue.setVisibility(View.GONE);
//            toolbarTextView.setText(getString(R.string.account));
            CommonMethods.textGradient(accountTitle, this);
            profile_image.setImageResource(R.drawable.user_icon_filled);
            profile_relative.setBackgroundColor(getResources().getColor(R.color.selectedTabBackground));
            home_image.setImageResource(R.drawable.home_icon);
            home_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            search_image.setImageResource(R.drawable.explore_icon);
            search_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            lifestyle_image.setImageResource(R.drawable.life_icon);
            lifestyle_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            cart_image.setImageResource(R.drawable.cart_icon);
            cart_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
        } else if (position == 4) {
//            cartValue.setVisibility(View.VISIBLE);
//            toolbarTextView.setText(getString(R.string.cart));


            CommonMethods.textGradient(cartTitle, this);
            cart_image.setImageResource(R.drawable.cart_icon_filled);
            cart_relative.setBackground(getResources().getDrawable(R.drawable.upper_right_round_selected_tab_background));
            home_image.setImageResource(R.drawable.home_icon);
            home_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            search_image.setImageResource(R.drawable.explore_icon);
            search_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            lifestyle_image.setImageResource(R.drawable.life_icon);
            lifestyle_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
            profile_image.setImageResource(R.drawable.user_icon);
            profile_relative.setBackgroundColor(getResources().getColor(R.color.unselectedTabBackground));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_relative:
                viewpager.setCurrentItem(0);
                break;
            case R.id.search_relative:
                viewpager.setCurrentItem(1);
                break;
            case R.id.lifestyle_relative:
                viewpager.setCurrentItem(2);
                break;
            case R.id.profile_relative:
                viewpager.setCurrentItem(3);
                break;
            case R.id.cart_relative:
                viewpager.setCurrentItem(4);
                break;
        }
    }

    /*
     * Update Cart Value
     */
    public void updateCartValue() {
        CommonMethods.showLog(TAG, "UPDATE CART METHOD CALLS");
        List<ProductDetailModel> list = databaseMethods.getAllProductList();
        if (list.size() > 0) {
            noOfCartItems.setVisibility(View.VISIBLE);
            noOfCartItems.setText(String.valueOf(list.size()));
        } else {
            noOfCartItems.setVisibility(View.GONE);
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

    private class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(MyConstants.TYPE);
            if (type.equalsIgnoreCase(MyConstants.UPDATE_CART_ITEMS)) {
                CommonMethods.showLog(TAG, "Broadcast Hit");
                updateCartValue();

            }
        }
    }
}
