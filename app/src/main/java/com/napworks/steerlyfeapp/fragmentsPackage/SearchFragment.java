package com.napworks.steerlyfeapp.fragmentsPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.SearchActivity;
import com.napworks.steerlyfeapp.adapterPackage.SearchViewPagerAdapter;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment implements View.OnClickListener {

    public SearchFragment() {
    }


    private String TAG = getClass().getSimpleName();

    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() != null) {

            List<String> tabsList = new ArrayList<>();
            tabsList.add(getString(R.string.categories));
            tabsList.add(getString(R.string.discover));

            SearchViewPagerAdapter searchViewPagerAdapter = new SearchViewPagerAdapter(getChildFragmentManager(), 0,
                    tabsList);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setAdapter(searchViewPagerAdapter);

            searchLayout.setOnClickListener(this);

        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (getActivity() != null) {
            if (v.getId() == R.id.searchLayout) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().startActivity(intent);
            }
        }
    }
}
