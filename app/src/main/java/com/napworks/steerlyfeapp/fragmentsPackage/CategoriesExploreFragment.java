package com.napworks.steerlyfeapp.fragmentsPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.adapterPackage.CategoriesExploreRecyclerViewAdapter;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesExploreFragment extends Fragment {


    private List<CategoryModel> categoriesList;
    private String TAG = getClass().getSimpleName();


    public CategoriesExploreFragment() {
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.message)
    TextView message;

    private CategoriesExploreRecyclerViewAdapter categoriesExploreRecyclerViewAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_categories_explore, container, false);
        ButterKnife.bind(this, view);
        categoriesList = new ArrayList<>();

        if (getActivity() != null) {
            DatabaseMethods databaseMethods = new DatabaseMethods(getActivity());
            categoriesList = databaseMethods.getAllCategoriesList();

            int screenWidth = CommonMethods.getWidth(getActivity()) / 2;
            int density = (int) CommonMethods.getScreenDensity(getActivity());
            screenWidth = screenWidth - (density*10);
//            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    CommonMethods.showLog(TAG, "Screen Listener Works");
//                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    int screenHeight = view.getHeight();
//
//                    screenHeight = screenHeight / 2;
//                    screenHeight = screenHeight - (density * 60);
//                    CommonMethods.showLog(TAG, "Scree Height : " + screenHeight);
//
//                    categoriesExploreRecyclerViewAdapter = new CategoriesExploreRecyclerViewAdapter(getActivity(), categoriesList, screenHeight, screenWidth);
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//                    recyclerView.setLayoutManager(gridLayoutManager);
//                    recyclerView.setAdapter(categoriesExploreRecyclerViewAdapter);
//
//                }
//            });

            if (categoriesList.size() == 0) {
                message.setVisibility(View.VISIBLE);
            } else {
                message.setVisibility(View.GONE);
                CommonMethods.showLog(TAG, "List Size : " + categoriesList.size());
                categoriesExploreRecyclerViewAdapter = new CategoriesExploreRecyclerViewAdapter(getActivity(), categoriesList, screenWidth);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(categoriesExploreRecyclerViewAdapter);
            }
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
