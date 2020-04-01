package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.DealsStoreDetailAdapter;
import com.napworks.steerlyfeapp.adapterPackage.RecentlyPostContentAdapter;
import com.napworks.steerlyfeapp.adapterPackage.SuggestedProductsAdapter;
import com.napworks.steerlyfeapp.designsPackage.CircularTransformation;
import com.napworks.steerlyfeapp.designsPackage.CustomReviewsContainer;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.dialogPackage.ReviewRatingDialog;
import com.napworks.steerlyfeapp.interfacePackage.CommonStringResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.ConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.RatingResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.StoreDetailResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CommonStringModel;
import com.napworks.steerlyfeapp.modelPackage.PostsInnerModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.ReviewsModel;
import com.napworks.steerlyfeapp.modelPackage.StoreDetailMainModel;
import com.napworks.steerlyfeapp.modelPackage.StoreDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StoreDetailActivity extends AppCompatActivity implements OnMapReadyCallback, StoreDetailResponseInterface,
        View.OnClickListener, RatingResponseInterface, CommonStringResponseInterface {

    private String TAG = getClass().getSimpleName();


    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.profileImage)
    ImageView profileImage;
    @BindView(R.id.storeName)
    TextView storeName;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.phoneNumberText)
    TextView phoneNumberText;
    @BindView(R.id.messageText)
    TextView messageText;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.loadMoreTextView)
    TextView loadMoreTextView;
    @BindView(R.id.writeAReviewText)
    TextView writeAReviewText;
    @BindView(R.id.menuText)
    TextView menuText;
    @BindView(R.id.reviewInflatedLayout)
    LinearLayout reviewInflatedLayout;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dealsRecyclerView)
    RecyclerView dealsRecyclerView;
    @BindView(R.id.recentlyPostRecyclerView)
    RecyclerView recentlyPostRecyclerView;

    List<ReviewsModel> reviewsList;
    private String subStoreId = "", storeId = "";
    private double currentLatitude = 30.710916, currentLongitutde = 76.686346;
    private GoogleMap mMap;
    BitmapDescriptor locationPin = null;
    LoadingDialog loadingDialog;
    SharedPreferences sharedPreferences;
    CommonMessageDialog commonMessageDialog;
    DealsStoreDetailAdapter dealsStoreDetailAdapter;
    RecentlyPostContentAdapter recentlyPostContentAdapter;

    ReviewRatingDialog reviewRatingDialog;

    private List<ProductDetailModel> dealsList;
    private List<PostsInnerModel> recentlyPostsList;

    private boolean isAlreadyReviewed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ButterKnife.bind(this);
        reviewsList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        commonMessageDialog = new CommonMessageDialog(this);
        locationPin = BitmapDescriptorFactory.fromResource(R.drawable.location_pin);
        dealsList = new ArrayList<>();
        recentlyPostsList = new ArrayList<>();
        reviewRatingDialog = new ReviewRatingDialog(this, this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (getIntent() != null) {
            subStoreId = getIntent().getStringExtra(MyConstants.SUB_STORE_ID);
            CommonMethods.showLog(TAG, "Sub Store  Id : " + subStoreId);
        }

        mainLinear.setVisibility(View.GONE);
        hitApi();

        int screenWidth = CommonMethods.getWidth(this);
        int density = (int) CommonMethods.getScreenDensity(this);
        screenWidth = (int) (screenWidth / 2.5);
        screenWidth = screenWidth - (density * 30);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dealsStoreDetailAdapter = new DealsStoreDetailAdapter(dealsList, this, screenWidth);
        dealsRecyclerView.setLayoutManager(linearLayoutManager);
        dealsRecyclerView.setAdapter(dealsStoreDetailAdapter);

        recentlyPostContentAdapter = new RecentlyPostContentAdapter(recentlyPostsList, this, screenWidth);
        LinearLayoutManager recentlyLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recentlyPostRecyclerView.setLayoutManager(recentlyLinearLayoutManager);
        recentlyPostRecyclerView.setAdapter(recentlyPostContentAdapter);


        writeAReviewText.setOnClickListener(this);
        menuText.setOnClickListener(this);
    }
    /*
     * Store Detail Api
     */
    private void hitApi() {
        loadingDialog.showDialog();
        CommonWebServices.getStoreDetail(sharedPreferences, subStoreId, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);

    }
    /*
     * Store Detail Api Response
     */
    @Override
    public void getStoreDetailResponse(String status, StoreDetailMainModel storeDetailMainModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (storeDetailMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Store Detail Api Status : " + storeDetailMainModel.getStatus());
                    switch (storeDetailMainModel.getStatus()) {
                        case "1":
                            mainLinear.setVisibility(View.VISIBLE);
                            CommonMethods.showLog(TAG, "Store Detail Api Success");
                            isAlreadyReviewed = storeDetailMainModel.getStoreDetail().isUserRated();
                            setData(storeDetailMainModel);
                            break;
                        case "2":
                            CommonMethods.showLog(TAG, "Store Detail Status 2 : " + storeDetailMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(storeDetailMainModel.getMessage());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.store_detail_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mainLayout) {
            CommonMethods.showLog(TAG, "Follow Clicked");
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }
    /*
     * Set Data
     */
    private void setData(StoreDetailMainModel storeDetailMainModel) {
        StoreDetailModel storeDetailModel = storeDetailMainModel.getStoreDetail();
        if (storeDetailModel != null) {
            subStoreId = storeDetailModel.getSubStoreId();
            storeId = storeDetailModel.getStoreId();

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.placeholder(R.drawable.logo);
            Glide.with(this)
                    .asBitmap()
                    .load(storeDetailModel.getStoreLogo())
                    .apply(requestOptions)
                    .into(new CircularTransformation(this, profileImage));


            requestOptions = new RequestOptions();
            requestOptions = requestOptions.placeholder(R.drawable.logo);
            Glide.with(this)
                    .asBitmap()
                    .load(storeDetailModel.getBannerUrl())
                    .apply(requestOptions)
                    .into(toolbarImage);

            rating.setRating(storeDetailModel.getStoreRating());
            storeName.setText(storeDetailModel.getStoreName());
            description.setText(storeDetailModel.getDescription());
            locationText.setText(storeDetailModel.getAddress());
            phoneNumberText.setText(storeDetailModel.getPhoneNumber());
            messageText.setText(storeDetailModel.getEmail());
            timeText.setText(CommonMethods.timeFormat(storeDetailModel.getOpeningTime()).
                    concat(" - ").
                    concat(CommonMethods.timeFormat(storeDetailModel.getClosingTime())));

            currentLatitude = storeDetailModel.getLat();
            currentLongitutde = storeDetailModel.getLng();
            mMap.addMarker(new MarkerOptions().position(new LatLng(
                    currentLatitude, currentLongitutde)).icon(locationPin));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitutde), 16));

            reviewsList.clear();
            reviewsList.addAll(storeDetailModel.getReviews());
            if (reviewsList.size() == 0) {
                loadMoreTextView.setVisibility(View.GONE);
                messageTextView.setVisibility(View.VISIBLE);
                messageTextView.setText(getString(R.string.noReviewsAvailable));
            } else {
                loadMoreTextView.setVisibility(View.VISIBLE);
                messageTextView.setVisibility(View.GONE);
                new CustomReviewsContainer(this, reviewsList, reviewInflatedLayout);
                String userId = sharedPreferences.getString(MyConstants.USER_ID, "");
                CommonMethods.showLog(TAG, "User Id : " + userId);
            }

            recentlyPostsList.addAll(storeDetailModel.getPostsList());
            CommonMethods.showLog(TAG,"Recently : "+recentlyPostsList.size());
            recentlyPostContentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writeAReviewText:
                if (isAlreadyReviewed) {
                    commonMessageDialog.showDialog(getString(R.string.youAlreadyWroteAReviewText));
                } else {
                    reviewRatingDialog.showDialog(getString(R.string.rateStore), MyConstants.STORE_REVIEWS, getString(R.string.cancel), getString(R.string.ok));
                }
                break;
            case R.id.menuText:
                Intent intent = new Intent(this, SubStoreProductsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(MyConstants.SUB_STORE_ID, subStoreId);
                intent.putExtra(MyConstants.STORE_ID, storeId);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstants.SUBMIT_REVIEW) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    CommonMethods.showLog(TAG, "Review Submitted");
                    hitApi();
                }
            }
        }
    }


    @Override
    public void getRatingResponse(String type, String comment, float rating) {

        if (type.equalsIgnoreCase(MyConstants.STORE_REVIEWS)) {
            loadingDialog.showDialog();
            CommonWebServices.submitReview(sharedPreferences, subStoreId, rating, comment, this);
        }

    }
    /*
     * Submit Review  Api Response
     */
    @Override
    public void getCommonStringResponse(String status, CommonStringModel commonStringModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (commonStringModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Submit Review Api Status : " + commonStringModel.getStatus());
                    switch (commonStringModel.getStatus()) {
                        case "1":
                            CommonMethods.showLog(TAG, "Submit Review Api Success");
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Update Data Status 2 : " + commonStringModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "0":
                            commonMessageDialog.showDialog(commonStringModel.getMessage());
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
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
}
