package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.designsPackage.CircularTransformation;
import com.napworks.steerlyfeapp.dialogPackage.ConfirmationDialog;
import com.napworks.steerlyfeapp.modelPackage.PostsInnerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedPostsRecyclerAdapter extends RecyclerView.Adapter {
    private final ConfirmationDialog confirmationDialog;
    private String TAG = getClass().getSimpleName();
    private final List<PostsInnerModel> postList;
    private final Activity activity;
    private final SharedPreferences sharedPreferences;
    private final FragmentManager supportFragmentManager;
    private int currentPosition = 0;

    public SavedPostsRecyclerAdapter(FragmentManager supportFragmentManager, Activity activity, ConfirmationDialog confirmationDialog, List<PostsInnerModel> postList, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.postList = postList;
        this.sharedPreferences = sharedPreferences;
        this.confirmationDialog = confirmationDialog;
        this.supportFragmentManager = supportFragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_saved_post, parent, false);
            return new PostClass(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_layout_swipe, parent, false);
            return new ProgressClass(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!postList.get(position).isLoadingType()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostsInnerModel postsInnerModel = postList.get(position);
        if (holder instanceof PostClass) {
            ((PostClass) holder).name.setText(postsInnerModel.getStoreName());
            ((PostClass) holder).postTime.setText(CommonMethods.formatTimestamp(String.valueOf(postsInnerModel.getCreatedTime())));
            ((PostClass) holder).description.setText(postsInnerModel.getTitle());
            ((PostClass) holder).views.setText(String.valueOf(postsInnerModel.getViews()));
            ((PostClass) holder).categoryName.setText(activity.getString(R.string.categoryName).
                    concat(" : ").
                    concat(postsInnerModel.getCategoryName()));
            ((PostClass) holder).saveButton.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_post_saved));
            Glide.with(activity)
                    .asBitmap()
                    .load(postsInnerModel.getStoreLogo())
                    .into(new CircularTransformation(activity, ((PostClass) holder).imageView));

            PostsViewPagerAdapter postsViewPagerAdapter = new PostsViewPagerAdapter(supportFragmentManager, 0, postList.get(position).getPostImages());
            ((PostClass) holder).viewPager.setAdapter(postsViewPagerAdapter);
            ((PostClass) holder).viewPager.setId((int) (Math.random() * 10000));
            ((PostClass) holder).tabLayout.setupWithViewPager(((PostClass) holder).viewPager, true);
        } else {
            ((ProgressClass) holder).progressLayout.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostClass extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        @BindView(R.id.categoryName)
        TextView categoryName;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.saveButton)
        ImageView saveButton;
        @BindView(R.id.shareButton)
        ImageView shareButton;
        private String action = "";

        PostClass(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            saveButton.setOnClickListener(this);
            shareButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            currentPosition = getAdapterPosition();
            if (currentPosition >= 0) {
                PostsInnerModel innerModel = postList.get(currentPosition);
                switch (v.getId()) {
                    case R.id.saveButton:
                        CommonMethods.showLog(TAG, "Click : " + currentPosition);
                        postList.remove(currentPosition);
                        confirmationDialog.showDialog(activity.getString(R.string.areYouSureRemovePostText),
                                innerModel.getPostId(),
                                activity.getString(R.string.cancel),
                                activity.getString(R.string.ok));

                        break;
                    case R.id.shareButton:
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String sharedLink = MyConstants.SHARED_LINK.concat(innerModel.getPostPublicId());
                        CommonMethods.showLog(TAG, "LINK : " + sharedLink);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharedLink);
                        activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;
                }
            }
        }
    }

    class ProgressClass extends RecyclerView.ViewHolder {
        @BindView(R.id.progressLayout)
        ProgressBar progressLayout;

        ProgressClass(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
