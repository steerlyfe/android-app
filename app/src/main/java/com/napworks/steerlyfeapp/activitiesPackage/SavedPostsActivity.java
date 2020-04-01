package com.napworks.steerlyfeapp.activitiesPackage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.SavedPostsRecyclerAdapter;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.ConfirmationDialog;
import com.napworks.steerlyfeapp.interfacePackage.ConfirmationDialogResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.PostsResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.PostsInnerModel;
import com.napworks.steerlyfeapp.modelPackage.PostsMainModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedPostsActivity extends ParentActivity implements SwipeRefreshLayout.OnRefreshListener, PostsResponseInterface, ConfirmationDialogResponseInterface {

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

    boolean last = true, retofitLoading = true, refresh = true;
    LinearLayoutManager linearLayoutManager;

    private SharedPreferences sharedPreferences;
    private CommonMessageDialog commonMessageDialog;
    private SavedPostsRecyclerAdapter savedPostsRecyclerAdapter;
    private List<PostsInnerModel> postList;
    ConfirmationDialog confirmationDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);
        ButterKnife.bind(this);
        postList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        confirmationDialog = new ConfirmationDialog(this, this);
        commonMessageDialog = new CommonMessageDialog(this);
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.savedPosts), toolbarText);

        savedPostsRecyclerAdapter = new SavedPostsRecyclerAdapter(getSupportFragmentManager(), this, confirmationDialog, postList, sharedPreferences);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(savedPostsRecyclerAdapter);

        swipeContainer.setOnRefreshListener(this);
        postList.clear();
        swipeContainer.setRefreshing(true);

        hitApi();
        scrollInitialize();

    }

    /*
     * Saved Posts Api
     */

    private void hitApi() {
        retofitLoading = true;
        int count = 0;
        if (!refresh) {
            if (postList.size() > 0) {
                count = postList.size() - 1;
            }
        }

        CommonWebServices.getSavedPosts(sharedPreferences,
                count,
                this);

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
        CommonMethods.showLog(TAG, "Chlda" + postList.size());
        postList.add(new PostsInnerModel("", "", "", 0, 0,
                "", "", "", "",false, null,
                true));
        savedPostsRecyclerAdapter.notifyItemInserted(postList.size() - 1);
    }

    private void checkAndShow() {
        if (postList.size() == 0) {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(getResources().getString(R.string.youHaveNotSavedAnyPostsYet));
        } else {
            messageTextView.setVisibility(View.GONE);

        }
    }

    /*
     * Saved Posts Api Response
     */

    @Override
    public void getPostsResponse(String status, PostsMainModel postsMainModel) {
        swipeContainer.setRefreshing(false);
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (postsMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "getPosts Api Status : " + postsMainModel.getStatus());
                    switch (postsMainModel.getStatus()) {
                        case "1":
                            if (refresh) {
                                savedPostsRecyclerAdapter.notifyItemRangeRemoved(0, postList.size());
                                postList.clear();
                                refresh = false;
                            }
                            if (postList.size() > 0) {
                                postList.remove(postList.size() - 1);
                                savedPostsRecyclerAdapter.notifyItemRemoved(postList.size());
                            }


                            postList.addAll(postsMainModel.getPostList());
                            savedPostsRecyclerAdapter.notifyItemRangeChanged(postList.size() - postsMainModel.getPostList().size(),
                                    postsMainModel.getPostList().size());
                            if (postsMainModel.getPostList().size() < postsMainModel.getPerPageItems()) {
                                CommonMethods.showLog(TAG, "last chal gya");
                                last = false;
                            } else {
                                last = true;
                            }
                            checkAndShow();
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "getPosts Api Status 2 : " + postsMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(postsMainModel.getMessage());
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

    @Override
    public void getConfirmationDialogResponse(String postId) {
        savedPostsRecyclerAdapter.notifyDataSetChanged();
        checkAndShow();
        CommonWebServices.savePost(sharedPreferences, postId, MyConstants.REMOVE);
    }
}
