package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.napworks.steerlyfeapp.modelPackage.PostsInnerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.CommonWebServices;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsRecyclerAdapter extends RecyclerView.Adapter {
    private final List<PostsInnerModel> postsList;
    private final Activity activity;
    private final SharedPreferences sharedPreferences;
    private final FragmentManager supportFragmentManager;
    private String TAG = getClass().getSimpleName();
    private int currentPosition = 0;


    public PostsRecyclerAdapter(FragmentManager supportFragmentManager, Activity activity, List<PostsInnerModel> postsList, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.postsList = postsList;
        this.sharedPreferences = sharedPreferences;
        this.supportFragmentManager = supportFragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(activity).inflate(R.layout.layout_post, parent, false);
            return new PostClass(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_layout_swipe, parent, false);
            return new ProgressClass(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostsInnerModel postsInnerModel = postsList.get(position);
        if (holder instanceof PostClass) {
            ((PostClass) holder).name.setText(postsInnerModel.getStoreName());
            ((PostClass) holder).postTime.setText(CommonMethods.formatTimestamp(String.valueOf(postsInnerModel.getCreatedTime())));
            ((PostClass) holder).description.setText(postsInnerModel.getTitle());
            ((PostClass) holder).views.setText(String.valueOf(postsInnerModel.getViews()));

            if (postsInnerModel.isPostSaved()) {
                ((PostClass) holder).saveButton.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_post_saved));
            } else {
                ((PostClass) holder).saveButton.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_unsaved_post));
            }


            Glide.with(activity)
                    .asBitmap()
                    .load(postsInnerModel.getStoreLogo())
                    .into(new CircularTransformation(activity, ((PostClass) holder).imageView));

            if (postsList.get(position).getPostImages().size() > 1) {
                ((PostClass) holder).tabLayout.setVisibility(View.VISIBLE);
            } else {
                ((PostClass) holder).tabLayout.setVisibility(View.INVISIBLE);
            }

            PostsViewPagerAdapter postsViewPagerAdapter = new PostsViewPagerAdapter(supportFragmentManager, 0, postsList.get(position).getPostImages());
            ((PostClass) holder).viewPager.setAdapter(postsViewPagerAdapter);
            ((PostClass) holder).viewPager.setId((int) (Math.random() * 10000));
            ((PostClass) holder).tabLayout.setupWithViewPager(((PostClass) holder).viewPager, true);


        } else {
            ((ProgressClass) holder).progressLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (!postsList.get(position).isLoadingType()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
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

        //        @BindView(R.id.postImage)
//        ImageView postImage;
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
                PostsInnerModel innerModel = postsList.get(currentPosition);
                switch (v.getId()) {
                    case R.id.saveButton:
                        CommonMethods.showLog(TAG, "Click : " + currentPosition);
                        if (innerModel.isPostSaved()) {
                            action = MyConstants.REMOVE;
                        } else {
                            action = MyConstants.SAVE;
                        }
                        CommonWebServices.savePost(sharedPreferences, innerModel.getPostId(), action);
                        innerModel.setPostSaved(!innerModel.isPostSaved());
                        notifyItemChanged(currentPosition);
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
