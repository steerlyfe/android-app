package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
//    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final Activity activity;
    private String TAG = getClass().getSimpleName();

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Activity activity) {
        super(fm, behavior);
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        CommonMethods.showLog(TAG, "Position : " + position + " Fragment No. : " + mFragmentList.get(position));
        return mFragmentList.get(position);
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mFragmentTitleList.get(position);
//    }

}
