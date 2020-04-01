package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.ProductsActivity;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesExploreRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesExploreRecyclerViewAdapter.MyViewHolder> {

    private final int screenWidth;
    private Activity activity;
    private List<CategoryModel> categoriesList;
    private String TAG = getClass().getSimpleName();
    private int currentPosition = 0;

    public CategoriesExploreRecyclerViewAdapter(Activity activity, List<CategoryModel> categoriesList, int screenWidth) {
        this.activity = activity;
        this.categoriesList = categoriesList;
        this.screenWidth = screenWidth;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_categories_explore, parent, false);
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
        LinearLayout mainLinear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) image.getLayoutParams();
            layoutParams.width = screenWidth + 1 - 1;
            layoutParams.height = screenWidth + 1 - 1;
            image.setLayoutParams(layoutParams);
            mainLinear.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.mainLinear) {
                currentPosition = getAdapterPosition();
                if (currentPosition >= 0) {
                    CommonMethods.showLog(TAG, "Clicked on : " + currentPosition);
                    Intent intent = new Intent(activity, ProductsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra(MyConstants.CATEGORY_ID, categoriesList.get(currentPosition).getCategoryId());
                    intent.putExtra(MyConstants.CATEGORY_NAME, categoriesList.get(currentPosition).getCategoryName());
                    activity.startActivityForResult(intent, MyConstants.CATEGORIES_EXPLORE_REQUEST_CODE);
                }
            }
        }
    }
}
