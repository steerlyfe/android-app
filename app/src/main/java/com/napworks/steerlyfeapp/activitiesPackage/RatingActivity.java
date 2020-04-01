package com.napworks.steerlyfeapp.activitiesPackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.OrdersRecyclerAdapter;
import com.napworks.steerlyfeapp.adapterPackage.RatingAdapter;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.CommonStringResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.OnRateProductItemClickedInterFace;
import com.napworks.steerlyfeapp.interfacePackage.ProductCategoryResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.RatingProductResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CommonStringModel;
import com.napworks.steerlyfeapp.modelPackage.ProductCategoriesMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.RatingInnerModel;
import com.napworks.steerlyfeapp.modelPackage.RatingMainModel;
import com.napworks.steerlyfeapp.modelPackage.RatingQuestionsModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingActivity extends ParentActivity implements SwipeRefreshLayout.OnRefreshListener, RatingProductResponseInterface, OnRateProductItemClickedInterFace {
    private String TAG = getClass().getSimpleName();
    private SharedPreferences sharedPreferences;
    private LoadingDialog loadingDialog;
    private CommonMessageDialog commonMessageDialog;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.messageTextView)
    TextView messageTextView;


    String callFrom = "";

    RatingAdapter ratingAdapter;
    private String orderId = "";

    List<RatingInnerModel> ratingQuestionsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        commonMessageDialog = new CommonMessageDialog(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.rating), toolbarText);
        ratingQuestionsList = new ArrayList<>();

        if (getIntent() != null) {
            callFrom = getIntent().getStringExtra(MyConstants.CALLED_FROM);
            orderId = getIntent().getStringExtra(MyConstants.ORDER_ID);
        }

        ratingAdapter = new RatingAdapter(this, ratingQuestionsList, commonMessageDialog, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ratingAdapter);

        swipeContainer.setOnRefreshListener(this);
        ratingQuestionsList.clear();
        swipeContainer.setRefreshing(true);

        hitApi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rating_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.submit) {
            if (validate()) {
                swipeContainer.setRefreshing(true);
                CommonWebServices.submitOrderReview(sharedPreferences, prepareJSON(),orderId , this, swipeContainer);
            } else {
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    /*
     * Prepare Json
     */

    private String prepareJSON() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < ratingQuestionsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            CommonMethods.showLog(TAG, "Rating : " + ratingQuestionsList.get(i).getRating());
            CommonMethods.showLog(TAG, "Question ID : " + ratingQuestionsList.get(i).getQuestion_id());
            try {
                jsonObject.put("question_id", ratingQuestionsList.get(i).getQuestion_id());
                jsonObject.put("rating", ratingQuestionsList.get(i).getRating());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        CommonMethods.showLog(TAG, "JSON : " + jsonArray.toString());
        return jsonArray.toString();
    }

    private boolean validate() {
        boolean value = true;
        for (int i = 0; i < ratingQuestionsList.size(); i++) {
            if (ratingQuestionsList.get(i).getRating() == 0.0) {
                commonMessageDialog.showDialog(getString(R.string.pleaseRate).
                        concat(" ").
                        concat(getString(R.string.commas)).
                        concat(ratingQuestionsList.get(i).getQuestion()).
                        concat(" ").
                        concat(getString(R.string.commas)));
                value = false;
                break;
            }
        }
        return value;
    }

    /*
     * Order Feedback Questions  Api
     */

    private void hitApi() {
        CommonWebServices.getOrderFeedbackQuestions(sharedPreferences,
                this);
    }

    @Override
    public void onRefresh() {
        ratingAdapter.notifyItemRangeRemoved(0, ratingQuestionsList.size());
        ratingQuestionsList.clear();
        swipeContainer.setRefreshing(true);
        hitApi();
    }

    private void checkAndShow() {
        if (ratingQuestionsList.size() == 0) {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(getResources().getString(R.string.noProductAvailableText));
        } else {
            messageTextView.setVisibility(View.GONE);

        }
    }


    /*
     * Order Feedback Api Response
     */

    @Override
    public void getRatingProductResponse(String status, RatingMainModel ratingMainModel) {
        swipeContainer.setRefreshing(false);
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (ratingMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Product Category Api Status : " + ratingMainModel.getStatus());
                    switch (ratingMainModel.getStatus()) {
                        case "1":
                            ratingQuestionsList.addAll(ratingMainModel.getQuestionList());
                            ratingAdapter.notifyDataSetChanged();
                            checkAndShow();
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Product Category Status 2 : " + ratingMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(ratingMainModel.getMessage());
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
    public void onRateProductItemClicked(int adapterPosition, List<RatingQuestionsModel> ratingList, RatingInnerModel ratingInnerModel) {
        CommonMethods.showLog(TAG, "Rating List Size : " + ratingList.size() + " at " + adapterPosition);
//
////        if (ratingLocalList.size() > 0) {
////            ratingLocalList.clear();
////        }
////        ratingLocalList.addAll(ratingList);
//        CommonMethods.showLog(TAG, "Rating List : " + ratingList.size());
//        CommonMethods.showLog(TAG, "Adapter  : " + adapterPosition);
//
//        for (int i = 0; i < ratingList.size(); i++) {
//            CommonMethods.showLog(TAG, "Rating : " + ratingList.get(i).getRating());
//            CommonMethods.showLog(TAG, "getQuestion_id : " + ratingList.get(i).getQuestion_id());
//        }
//
//        productList.get(adapterPosition).setLocalList(ratingList);
//        ratingAdapter.notifyItemChanged(adapterPosition);
////        for (int i = 0; i < productList.size(); i++) {
////            RatingInnerModel innerModel = productList.get(i);
////            if (innerModel.getProduct_id().equalsIgnoreCase(ratingInnerModel.getProduct_id()) &&
////                    innerModel.getProduct_availability().equalsIgnoreCase(ratingInnerModel.getProduct_availability()) &&
////                    innerModel.getAdditional_feature().equalsIgnoreCase(ratingInnerModel.getAdditional_feature())) {
////                CommonMethods.showLog(TAG, "Match At : " + i);
////                innerModel.setLocalList(ratingLocalList);
////                ratingAdapter.notifyItemChanged(i);
////                CommonMethods.showLog(TAG, "LIST SIZE : " + innerModel.getLocalList().size());
////            }
////        }

    }
}
