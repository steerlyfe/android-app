package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.napworks.steerlyfeapp.fragmentsPackage.DiscoverExploreFragment;
import com.napworks.steerlyfeapp.fragmentsPackage.CategoriesExploreFragment;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.List;

public class SearchViewPagerAdapter extends FragmentStatePagerAdapter {

    Activity activity;
    List<String> tabsList;
    private String TAG = getClass().getSimpleName();

    public SearchViewPagerAdapter(FragmentManager fm, int behavior, List<String> tabsList) {
        super(fm, behavior);
        this.tabsList = tabsList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        CommonMethods.showLog(TAG, "Works");
        Fragment fragment;
        if (position == 0) {
            fragment = new CategoriesExploreFragment();
        } else {
            fragment = new DiscoverExploreFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabsList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabsList.get(position);
    }

}
