package com.napworks.steerlyfeapp.adapterPackage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.napworks.steerlyfeapp.fragmentsPackage.PostsImagesFragment;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

public class PostsViewPagerAdapter extends FragmentPagerAdapter {

    private final List<String> postImagesList;
    private String TAG = getClass().getSimpleName();

    public PostsViewPagerAdapter(FragmentManager fm, int behaviour, List<String> postImagesList) {
        super(fm,behaviour);
        this.postImagesList = postImagesList;
        CommonMethods.showLog(TAG, "sizeOfList " + this.postImagesList.size());
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
//        CommonMethods.showLog(TAG, "Image" + postImagesList.get(i));
        Fragment fragment = new PostsImagesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MyConstants.POST_IMAGE, postImagesList.get(i));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return postImagesList.size();
    }

}
