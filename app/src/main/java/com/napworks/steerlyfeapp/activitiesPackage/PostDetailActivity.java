package com.napworks.steerlyfeapp.activitiesPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.PostsRecyclerAdapter;
import com.napworks.steerlyfeapp.adapterPackage.PostsViewPagerAdapter;
import com.napworks.steerlyfeapp.designsPackage.CircularTransformation;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.dialogPackage.LoadingDialog;
import com.napworks.steerlyfeapp.interfacePackage.PostDetailResponseInterface;
import com.napworks.steerlyfeapp.interfacePackage.ProductDetailResponseInterface;
import com.napworks.steerlyfeapp.modelPackage.PostDetailInnerModel;
import com.napworks.steerlyfeapp.modelPackage.PostDetailMainModel;
import com.napworks.steerlyfeapp.modelPackage.PostsInnerModel;
import com.napworks.steerlyfeapp.modelPackage.PostsMainModel;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailMainModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailActivity extends ParentActivity implements PostDetailResponseInterface, View.OnClickListener {

    private String TAG = getClass().getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTextView)
    TextView toolbarText;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.views)
    TextView views;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.postTime)
    TextView postTime;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.saveButton)
    ImageView saveButton;
    @BindView(R.id.shareButton)
    ImageView shareButton;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;

    private String action = "";

    private String postId = "";
    LoadingDialog loadingDialog;
    SharedPreferences sharedPreferences;
    CommonMessageDialog commonMessageDialog;
    PostDetailInnerModel postDetailInnerModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        postDetailInnerModel = new PostDetailInnerModel();
        CommonMethods.toolbarInitialize(this, toolbar, getString(R.string.postDetail), toolbarText);
        sharedPreferences = getSharedPreferences(MyConstants.SHARED_PREFERENCE, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this);
        if (getIntent() != null) {
            postId = getIntent().getStringExtra(MyConstants.POST_ID);
        }

        mainLinear.setVisibility(View.GONE);
        hitApi();
    }

    /*
     * Post Detail Api
     */
    private void hitApi() {
        loadingDialog.showDialog();
        CommonWebServices.getPostDetail(sharedPreferences, postId, this);
    }

    /*
     * Post Detail Api Response
     */

    @Override
    public void getPostDetailResponse(String status, PostDetailMainModel postDetailMainModel) {
        loadingDialog.hideDialog();
        switch (status) {
            case MyConstants.GO_TO_RESULT:
                if (postDetailMainModel.getStatus() != null) {
                    CommonMethods.showLog(TAG, "Post Detail Api Status : " + postDetailMainModel.getStatus());
                    switch (postDetailMainModel.getStatus()) {
                        case "1":
                            mainLinear.setVisibility(View.VISIBLE);
                            CommonMethods.showLog(TAG, "Post Detail Api Success");
                            postDetailInnerModel = postDetailMainModel.getPostDetail();
                            setData(postDetailMainModel.getPostDetail());
                            break;

                        case "2":
                            CommonMethods.showLog(TAG, "Post Detail Status 2 : " + postDetailMainModel.getMessage());
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;

                        case "11":
                            commonMessageDialog.showDialog(getString(R.string.some_errors_occurred_text));
                            break;
                        case "10":
                            CommonMethods.sessionOut(this);
                            break;
                        case "0":
                            commonMessageDialog.showDialog(postDetailMainModel.getMessage());
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

    /*
     * This method is for set data
     */
    private void setData(PostDetailInnerModel postDetailInnerModel) {

        name.setText(postDetailInnerModel.getStoreName());
        postTime.setText(CommonMethods.formatTimestamp(String.valueOf(postDetailInnerModel.getCreatedTime())));
        description.setText(postDetailInnerModel.getTitle());
        views.setText(String.valueOf(postDetailInnerModel.getViews()));

        if (postDetailInnerModel.isPostSaved()) {
            saveButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_post_saved));
        } else {
            saveButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_unsaved_post));
        }


        Glide.with(this)
                .asBitmap()
                .load(postDetailInnerModel.getStoreLogo())
                .into(new CircularTransformation(this, imageView));

        if (postDetailInnerModel.getPostImages().size() > 1) {
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            tabLayout.setVisibility(View.INVISIBLE);
        }

        PostsViewPagerAdapter postsViewPagerAdapter = new PostsViewPagerAdapter(getSupportFragmentManager(), 0, postDetailInnerModel.getPostImages());
        viewPager.setAdapter(postsViewPagerAdapter);
        viewPager.setId((int) (Math.random() * 10000));
        tabLayout.setupWithViewPager(viewPager, true);

        saveButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                if (postDetailInnerModel.isPostSaved()) {
                    action = MyConstants.REMOVE;
                    saveButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_unsaved_post));
                } else {
                    action = MyConstants.SAVE;
                    saveButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_post_saved));
                }
                CommonWebServices.savePost(sharedPreferences, postDetailInnerModel.getPostId(), action);
                postDetailInnerModel.setPostSaved(!postDetailInnerModel.isPostSaved());

                break;

            case R.id.shareButton:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
        }
    }
}
