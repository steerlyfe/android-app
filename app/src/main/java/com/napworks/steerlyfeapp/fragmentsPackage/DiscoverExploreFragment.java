package com.napworks.steerlyfeapp.fragmentsPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.DiscoverExploreRecyclerViewAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverExploreFragment extends Fragment {
    private List<CategoryModel> categoriesList;
    private String TAG = getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.message)
    TextView message;

    DiscoverExploreRecyclerViewAdapter discoverExploreRecyclerViewAdapter;
    private int screenWidth = 0;
    private int screenHeight = 0;

    public DiscoverExploreFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_discover_explore, container, false);
        ButterKnife.bind(this, view);
        categoriesList = new ArrayList<>();
        if (getActivity() != null) {
            DatabaseMethods databaseMethods = new DatabaseMethods(getActivity());
            categoriesList = databaseMethods.getAllCategoriesList();

            int density = (int) CommonMethods.getScreenDensity(getActivity());
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    CommonMethods.showLog(TAG, "Screen Listener Works");
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    screenHeight = view.getHeight();

                    screenHeight = screenHeight / 2;
                    screenHeight = screenHeight - (density * 20);
                    CommonMethods.showLog(TAG, "Scree Height : " + screenHeight);

                    discoverExploreRecyclerViewAdapter = new DiscoverExploreRecyclerViewAdapter(getActivity(), categoriesList, screenHeight, screenWidth);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(discoverExploreRecyclerViewAdapter);

                }
            });

            screenWidth = CommonMethods.getWidth(getActivity());
            CommonMethods.showLog(TAG, "Screen Width : " + screenWidth);
        }


//        if (categoriesList.size() == 0) {
//            message.setVisibility(View.VISIBLE);
//        } else {
//            message.setVisibility(View.GONE);
//            CommonMethods.showLog(TAG, "List Size : " + categoriesList.size());
//
//
//            discoverExploreRecyclerViewAdapter = new DiscoverExploreRecyclerViewAdapter(activity, categoriesList, screenHeight, screenWidth);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
//            recyclerView.setLayoutManager(linearLayoutManager);
//            recyclerView.setAdapter(discoverExploreRecyclerViewAdapter);
//
//        }
        return view;
    }
}
