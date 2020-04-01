package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.activitiesPackage.StoreDetailActivity;
import com.napworks.steerlyfeapp.interfacePackage.OnRecyclerItemClickedInterFace;
import com.napworks.steerlyfeapp.modelPackage.StoreDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearByStoresAdapter extends RecyclerView.Adapter<NearByStoresAdapter.MyViewHolder> {
    private final Activity activity;
    private final OnRecyclerItemClickedInterFace onRecyclerItemClickedInterFace;
    private final int width;
    private List<StoreDetailModel> storesList;
    private String TAG = getClass().getSimpleName();

    public NearByStoresAdapter(Activity activity, List<StoreDetailModel> storesList, int width,
                               OnRecyclerItemClickedInterFace onRecyclerItemClickedInterFace) {
        this.activity = activity;
        this.storesList = storesList;
        this.width = width;
        this.onRecyclerItemClickedInterFace = onRecyclerItemClickedInterFace;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.near_by_stores_list_recycler_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CommonMethods.showLog(TAG, "storeId " + storesList.get(position).getStoreId());
        Glide.with(activity)
                .asBitmap()
                .load(storesList.get(position).getStoreLogo())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.profileImage);

        holder.address.setText(storesList.get(position).getAddress());
        holder.name.setText(storesList.get(position).getStoreName());
//        holder.rating.setRating(Float.parseFloat(storesList.get(position).getStoreRating()));
        holder.rating.setRating(4);

        if (storesList.get(position).isSelected()) {
            holder.mainLinear.setBackground(ContextCompat.getDrawable(activity, R.drawable.color_primary_border_thin_design));
        } else {
            holder.mainLinear.setBackground(ContextCompat.getDrawable(activity, R.drawable.grey_border_thin_design));
        }
    }

    @Override
    public int getItemCount() {
        return storesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.mainLinear)
        LinearLayout mainLinear;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.rating)
        RatingBar rating;
        @BindView(R.id.profileImage)
        ImageView profileImage;
        @BindView(R.id.moreDetail)
        TextView moreDetail;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            moreDetail.setOnClickListener(this);
            mainLinear.setOnClickListener(this);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mainLinear.getLayoutParams();
            layoutParams.width = width;
            mainLinear.setLayoutParams(layoutParams);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() >= 0) {
                switch (v.getId()) {
                    case R.id.moreDetail:
                        CommonMethods.showLog(TAG, "Sub StoreId  " + storesList.get(getAdapterPosition()).getSubStoreId());
                        Intent i = new Intent(activity, StoreDetailActivity.class);
                        i.putExtra(MyConstants.SUB_STORE_ID, storesList.get(getAdapterPosition()).getSubStoreId());
                        activity.startActivity(i);
                        break;
                    case R.id.mainLinear:
                        onRecyclerItemClickedInterFace.onRecyclerItemClicked(getAdapterPosition(), "");
                        break;
                }
            }
        }

    }
}
