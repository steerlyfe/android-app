package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.databasePackage.DatabaseMethods;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity activity;
    private final List<ProductDetailModel> productList;
    private final CommonMessageDialog commonMessageDialog;
    private final DatabaseMethods databaseMethods;
    private final String callFrom;
    private String TAG = getClass().getSimpleName();

    public SearchAdapter(Activity activity, List<ProductDetailModel> productList,
                         CommonMessageDialog commonMessageDialog, DatabaseMethods databaseMethods, String callFrom) {
        this.activity = activity;
        this.commonMessageDialog = commonMessageDialog;
        this.productList = productList;
        this.databaseMethods = databaseMethods;
        this.callFrom = callFrom;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_product, parent, false);
            return new ProductDataClass(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_layout_swipe, parent, false);
            return new ProgressClass(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductDataClass) {
            ProductDetailModel productDetailModel = productList.get(position);
            int count = databaseMethods.getProductQuantity(productDetailModel.getProduct_id());
            productDetailModel.setProduct_quantity(count);
            CommonMethods.showLog(TAG, "Quantity : " + productDetailModel.getProduct_quantity());

            ((ProductDataClass) holder).productName.setText(productDetailModel.getProduct_name());
            ((ProductDataClass) holder).productPrice.setText(activity.getString(R.string.DOLLAR_SIGN)
                    .concat(" ")
                    .concat(String.valueOf(productDetailModel.getActual_price())));
            ((ProductDataClass) holder).noOfProductsTextView.setText(String.valueOf(productDetailModel.getProduct_quantity()));
            ((ProductDataClass) holder).productDescription.setText(productDetailModel.getDescription());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new RoundedCorners(30));
            Glide.with(activity)
                    .asBitmap()
                    .load(productDetailModel.getImage_url())
                    .apply(requestOptions)
                    .into(((ProductDataClass) holder).image);

        } else if (holder instanceof ProgressClass) {
            ((ProgressClass) holder).progressLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!productList.get(position).isLoadingType()) {
            return 1;
        } else {
            return 2;
        }
    }

    class ProductDataClass extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.plus)
        ImageView plus;
        @BindView(R.id.minus)
        ImageView minus;
        @BindView(R.id.noOfProductsTextView)
        TextView noOfProductsTextView;
        @BindView(R.id.productName)
        TextView productName;
        @BindView(R.id.productPrice)
        TextView productPrice;
        @BindView(R.id.productDescription)
        TextView productDescription;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.mainLinear)
        LinearLayout mainLinear;

        ProductDataClass(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            productName.setSelected(true);
            mainLinear.setOnClickListener(this);
            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int currentPosition = getAdapterPosition();
            if (currentPosition >= 0) {
                ProductDetailModel productDetailModel = productList.get(currentPosition);
                switch (v.getId()) {
                    case R.id.plus:
                        productDetailModel.setProduct_quantity(productDetailModel.getProduct_quantity() + 1);
                        boolean finalResult = databaseMethods.addOrUpdate(productDetailModel,true);
                        CommonMethods.showLog(TAG, "final : " + finalResult);
                        if (finalResult) {
                            notifyItemChanged(currentPosition);
                        }
                        break;
                    case R.id.minus:
                        if (productDetailModel.getProduct_quantity() > 0) {
                            productDetailModel.setProduct_quantity(productDetailModel.getProduct_quantity() - 1);
                            boolean result = databaseMethods.addOrUpdate(productDetailModel,false);
                            if (result) {
                                notifyItemChanged(currentPosition);
                            }
                        }
                        break;


                }
            }
        }
    }

    class ProgressClass extends RecyclerView.ViewHolder {
        @BindView(R.id.progressLayout)
        ProgressBar progressLayout;

        ProgressClass(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
