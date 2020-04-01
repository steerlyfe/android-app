package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.interfacePackage.CustomerHomeAdapterInterface;
import com.napworks.steerlyfeapp.modelPackage.CategoryModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.MyViewHolder> {

    private final CustomerHomeAdapterInterface customerHomeAdapterInterface;
    private String TAG = getClass().getSimpleName();
    private final Activity activity;
    private final List<CategoryModel> categoriesList;

    boolean selected = false;
    int previousPosition = 0;

    public CategoriesRecyclerAdapter(Activity activity, List<CategoryModel> categoriesList, CustomerHomeAdapterInterface customerHomeAdapterInterface) {
        this.activity = activity;
        this.categoriesList = categoriesList;
        this.customerHomeAdapterInterface = customerHomeAdapterInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_categories_recycler, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryModel categoryModel = categoriesList.get(position);
        holder.title.setText(categoryModel.getCategoryName());
        if (categoryModel.isSelected()) {
            previousPosition = position;
            holder.title.setBackground(ContextCompat.getDrawable(activity, R.drawable.black_background_round));
            holder.title.setTextColor(ContextCompat.getColor(activity, R.color.white));
        } else {
            holder.title.setBackground(ContextCompat.getDrawable(activity, R.drawable.grey_background_round));
            holder.title.setTextColor(ContextCompat.getColor(activity, R.color.black));
        }
    }


    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        TextView title;
//        @BindView(R.id.name)
//        TextView name;
//        @BindView(R.id.topLinearLayout)
//        LinearLayout topLinearLayout;
//        @BindView(R.id.image)
//        ImageView image;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.title) {
                categoriesList.get(previousPosition).setSelected(false);
                notifyItemChanged(previousPosition);
                CommonMethods.showLog(TAG, "Click On : " + getAdapterPosition());
                CommonMethods.showLog(TAG, "Click On : " + categoriesList.get(getAdapterPosition()).isSelected());

                categoriesList.get(getAdapterPosition()).setSelected(!categoriesList.get(getAdapterPosition()).isSelected());
                notifyItemChanged(getAdapterPosition());

//                if (categoriesList.get(getAdapterPosition()).isSelected()) {
//                    categoriesList.get(getAdapterPosition()).setSelected(false);
////                    title.setBackground(ContextCompat.getDrawable(activity, R.drawable.black_background_round));
//                    notifyItemChanged(getAdapterPosition());
//                } else {
////                    title.setBackground(ContextCompat.getDrawable(activity, R.drawable.grey_background_round));
//                    categoriesList.get(getAdapterPosition()).setSelected(true);
//                    notifyItemChanged(getAdapterPosition());
//                }
                previousPosition = getAdapterPosition();
                CommonMethods.showLog(TAG, "CategoryId : " + categoriesList.get(getAdapterPosition()).getCategoryId());
                customerHomeAdapterInterface.customerHomeAdapterInterface("1", getAdapterPosition(),
                        String.valueOf(categoriesList.get(getAdapterPosition()).getCategoryId()));
            }


        }
    }
}
