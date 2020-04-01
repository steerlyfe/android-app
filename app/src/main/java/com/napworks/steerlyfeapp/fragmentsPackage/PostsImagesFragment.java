package com.napworks.steerlyfeapp.fragmentsPackage;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.internal.service.Common;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.PostsViewPagerAdapter;
import com.napworks.steerlyfeapp.designsPackage.CircularTransformation;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsImagesFragment extends Fragment {

    @BindView(R.id.image)
    ImageView image;
    private String postImage = "";
    private String TAG = getClass().getSimpleName();


    public PostsImagesFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_images, container, false);
        ButterKnife.bind(this, view);
        CommonMethods.showLog(TAG, "Works");
        if (getActivity() != null) {
            if (getArguments() != null) {
                postImage = getArguments().getString(MyConstants.POST_IMAGE);
            }
            CommonMethods.showLog(TAG, "postImage : " + postImage);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.placeholder(R.drawable.app_logo);
            requestOptions = requestOptions.transforms(new RoundedCorners(30));
            Glide.with(getActivity())
                    .load(postImage)
                    .apply(requestOptions)
                    .into(image);
        }
        return view;
    }
}
