package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverExploreRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverExploreRecyclerViewAdapter.MyViewHolder> {
    private int screenWidth;
    private int screenHeight;
    Activity activity;
    List<CategoryModel> categoriesList;
    private String TAG = getClass().getSimpleName();
    private int currentPosition = 0;

    public DiscoverExploreRecyclerViewAdapter() {
    }

    public DiscoverExploreRecyclerViewAdapter(Activity activity, List<CategoryModel> categoriesList, int screenHeight, int screenWidth) {
        this.activity = activity;
        this.categoriesList = categoriesList;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        CommonMethods.showLog(TAG, "Screen Height : " + screenHeight);
        CommonMethods.showLog(TAG, "Screen Width : " + screenWidth);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_discover_explore, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryModel categoryModel = categoriesList.get(position);
        holder.categoryName.setText(categoryModel.getCategoryName());


        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(30));
        CommonMethods.showLog(TAG, "Image : " + categoryModel.getCategoryUrl());
        Glide.with(activity).
                load(categoryModel.getCategoryUrl()).
                apply(requestOptions).
                into((holder).image);

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.categoryName)
        TextView categoryName;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.mainLinear)
        FrameLayout mainLinear;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            FrameLayout.LayoutParams layoutParams = (CardView.LayoutParams) image.getLayoutParams();
            layoutParams.width = screenWidth + 1 - 1;
            layoutParams.height = screenHeight + 1 - 1;
            image.setLayoutParams(layoutParams);
            mainLinear.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.mainLinear) {
                currentPosition = getAdapterPosition();
                if (currentPosition > 0) {
                    CommonMethods.showLog(TAG, "Clicked on : " + currentPosition);
                }
            }
        }
    }
}
