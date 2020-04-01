package com.napworks.steerlyfeapp.fragmentsPackage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.MainActivity;
import com.napworks.steerlyfeapp.adapterPackage.CategoriesRecyclerAdapter;
import com.napworks.steerlyfeapp.adapterPackage.PostsRecyclerAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.CustomerHomeAdapterInterface;
import com.napworks.steerlyfeapp.interfacePackage.PostsResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.modelPackage.PostsInnerModel;
import com.napworks.steerlyfeapp.modelPackage.PostsMainModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LifestyleFragmentUpdated extends Fragment implements CustomerHomeAdapterInterface, PostsResponseInterface, SwipeRefreshLayout.OnRefreshListener {
    private String TAG = getClass().getSimpleName();
    private String categoryName = "Beauties";
    private String categoryId = "";

    public LifestyleFragmentUpdated() {
    }

    @BindView(R.id.browseCategoriesRecyclerView)
    RecyclerView browseCategoriesRecyclerView;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.postsRecyclerView)
    RecyclerView postsRecyclerView;
    @BindView(R.id.postsMessageTextView)
    TextView postsMessageTextView;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private CategoriesRecyclerAdapter categoriesRecyclerAdapter;
    public PostsRecyclerAdapter postsRecyclerAdapter;
    private List<CategoryModel> categoriesList;

    DatabaseMethods databaseMethods;
    private List<CategoryModel> databaseCategoriesList;
    private List<PostsInnerModel> postsList;
    LinearLayoutManager linearLayoutManager;


    SharedPreferences sharedPreferences;
    LoadingDialog loadingDialog;
    CommonMessageDialog commonMessageDialog;
    boolean last = true, retofitLoading = true, refresh = true;
    String postId = "";

    List<String> alreadyViewedList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lifestyle_updated, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
            commonMessageDialog = new CommonMessageDialog(getActivity());
            loadingDialog = new LoadingDialog(getActivity());
            databaseMethods = new DatabaseMethods(getActivity());
            categoriesList = new ArrayList<>();
            databaseCategoriesList = new ArrayList<>();
            postsList = new ArrayList<>();
            alreadyViewedList = new ArrayList<>();

//            ((MainActivity) getActivity()).updateStatusBarColor("#ffffff");

            databaseCategoriesList.addAll(databaseMethods.getAllCategoriesList());
            CommonMethods.showLog(TAG, "Databse Cat List : " + databaseCategoriesList.size());

//            setStatusBarGradiant(getActivity());

            for (int i = 0; i < databaseCategoriesList.size(); i++) {
                CategoryModel innerModel = databaseCategoriesList.get(i);
                if (innerModel.getCategoryName().equalsIgnoreCase(categoryName)) {
//                  categoryId = innerModel.getCategoryId();
                    categoriesList.add(new CategoryModel(innerModel.getCategoryId(), innerModel.getCategoryName(), innerModel.getCategoryUrl(), true));
                    categoryId = innerModel.getCategoryId();
                } else {
                    categoriesList.add(new CategoryModel(innerModel.getCategoryId(), innerModel.getCategoryName(), innerModel.getCategoryUrl(), false));
                }
            }

            browseCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            categoriesRecyclerAdapter = new CategoriesRecyclerAdapter(getActivity(), categoriesList, this);
            browseCategoriesRecyclerView.setAdapter(categoriesRecyclerAdapter);


            linearLayoutManager = new LinearLayoutManager(getActivity());
            postsRecyclerView.setLayoutManager(linearLayoutManager);
            postsRecyclerAdapter = new PostsRecyclerAdapter(getActivity().getSupportFragmentManager(), getActivity(), postsList, sharedPreferences);
            postsRecyclerView.setAdapter(postsRecyclerAdapter);

            swipeContainer.setOnRefreshListener(this);
            postsList.clear();
            swipeContainer.setRefreshing(true);
//            mainLinear.setVisibility(View.GONE);
            hitApi();
            scrollInitialize();


        }

        return view;
    }

    public FragmentManager getActivityFragmentManager() {
        if (getActivity() != null) {
            return getActivity().getSupportFragmentManager();
        } else {
            return null;
        }
    }

    private void hitApi() {
        retofitLoading = true;
        int count = 0;
        if (!refresh) {
            if (postsList.size() > 0) {
                count = postsList.size() - 1;
            }
        }


        CommonWebServices.getPosts(sharedPreferences,
                count,
                categoryId,
                this);


    }

    @Override
    public void onRefresh() {
//        onPause();
        alreadyViewedList.clear();
        for (int i = 0; i < postsList.size(); i++) {
            if (postsList.get(i).isViewStatusUpdated()) {
                alreadyViewedList.add(postsList.get(i).getPostId());
            }
        }
        CommonMethods.showLog(TAG, "Already Viewed List Size : " + alreadyViewedList.size());
        postsList.clear();
        postsRecyclerAdapter.notifyItemRangeRemoved(0, postsList.size());
        refresh = true;
        last = true;
        hitApi();
    }


    @Override
    public void onPause() {
        super.onPause();
        CommonMethods.showLog(TAG, "ON PAUSE");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < postsList.size(); i++) {
            if (postsList.get(i).isViewStatusUpdated()) {
                jsonArray.put(postsList.get(i).getPostId());
            }
        }

        CommonWebServices.updatePostViews(sharedPreferences, jsonArray.toString());

        CommonMethods.showLog(TAG, "JSON :" + jsonArray.toString());
    }

    private void scrollInitialize() {

        postsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int total = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastVisibleItemPosition();
                int firstVisible = linearLayoutManager.findFirstVisibleItemPosition();
                CommonMethods.showLog(TAG, "Last Visible : " + lastVisible + " Post Id : " + postId + " First Visible : " + firstVisible);


                if (firstVisible >= 0) {
                    CommonMethods.showLog(TAG, "Already LIST SIZE : " + alreadyViewedList.size() + " POST LIST : " + postsList.size());


                    if (alreadyViewedList.size() > 0) {
                        for (int i = 0; i < alreadyViewedList.size(); i++) {
                            if (postsList.get(firstVisible).getPostId().equalsIgnoreCase(alreadyViewedList.get(i))) {
                                postsList.get(firstVisible).setViewStatusUpdated(true);
                            } else if (lastVisible == i) {
                                if (postsList.get(lastVisible).getPostId().equalsIgnoreCase(alreadyViewedList.get(i))) {
                                    postsList.get(lastVisible).setViewStatusUpdated(true);
                                }
                            }
                        }
                    }

                    if (!postsList.get(firstVisible).isViewStatusUpdated()) {
                        postsList.get(firstVisible).setViewStatusUpdated(true);
                    } else if (lastVisible == postsList.size() - 1) {
                        CommonMethods.showLog(TAG, "ELSE IF ");
                        if (!postsList.get(lastVisible).isViewStatusUpdated()) {
                            postsList.get(lastVisible).setViewStatusUpdated(true);
                        }
                    }
                }

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
        CommonMethods.showLog(TAG, "Chlda" + postsList.size());
        postsList.add(new PostsInnerModel("", "", "", 0, 0,
                "", "", "", "", false, null, true));
        postsRecyclerAdapter.notifyItemInserted(postsList.size() - 1);
    }


    @Override
    public void customerHomeAdapterInterface(String status, int position, String categoryId) {
        switch (status) {
            case "1":
                this.categoryId = categoryId;
                CommonMethods.showLog(TAG, "CategoryId : " + categoryId);
                swipeContainer.setRefreshing(true);
                onRefresh();
        }
    }

    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            } else {
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
//            }
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.black));
        }
    }

    @Override
    public void getPostsResponse(String status, PostsMainModel postsMainModel) {
        mainLinear.setVisibility(View.VISIBLE);
        swipeContainer.setRefreshing(false);
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (postsMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Product Category Api Status : " + postsMainModel.getStatus());
                    switch (postsMainModel.getStatus()) {
                        case "1":
                            if (refresh) {
                                postsRecyclerAdapter.notifyItemRangeRemoved(0, postsList.size());
                                postsList.clear();
                                refresh = false;
                            }
                            if (postsList.size() > 0) {
                                postsList.remove(postsList.size() - 1);
                                postsRecyclerAdapter.notifyItemRemoved(postsList.size());
                            }

                            postsList.addAll(postsMainModel.getPostList());
                            postsRecyclerAdapter.notifyItemRangeChanged(postsList.size() - postsMainModel.getPostList().size(),
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
                            CommonMethods.showLog(TAG, "Product Category Status 2 : " + postsMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(getActivity());
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

    private void checkAndShow() {
        if (postsList.size() == 0) {
            postsMessageTextView.setVisibility(View.VISIBLE);
            postsMessageTextView.setText(getResources().getString(R.string.noPostAvailableYet));
        } else {
            postsMessageTextView.setVisibility(View.GONE);
        }
    }
}
