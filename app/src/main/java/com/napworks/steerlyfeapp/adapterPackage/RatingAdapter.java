package com.napworks.steerlyfeapp.adapterPackage;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.dialogPackage.CommonMessageDialog;
import com.napworks.steerlyfeapp.interfacePackage.OnRateProductItemClickedInterFace;
import com.napworks.steerlyfeapp.modelPackage.RatingInnerModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.DataClass> {
    private final OnRateProductItemClickedInterFace onRateProductItemClickedInterFace;
    private CommonMessageDialog commonMessageDialog;
    private Activity activity;
    private List<RatingInnerModel> ratingQuestionsList;
    private int currentPosition = 0;
    private String TAG = getClass().getSimpleName();

    public RatingAdapter(Activity activity, List<RatingInnerModel> ratingQuestionsList, CommonMessageDialog commonMessageDialog,
                         OnRateProductItemClickedInterFace onRateProductItemClickedInterFace) {
        this.activity = activity;
        this.commonMessageDialog = commonMessageDialog;
        this.onRateProductItemClickedInterFace = onRateProductItemClickedInterFace;
        this.ratingQuestionsList = ratingQuestionsList;
    }

    @NonNull
    @Override
    public DataClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rating, parent, false);
        return new DataClass(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DataClass holder, int position) {
        RatingInnerModel ratingInnerModel = ratingQuestionsList.get(position);
        holder.questionDesc.setText(ratingInnerModel.getQuestion());
        holder.rating.setRating(ratingInnerModel.getRating());

    }


    @Override
    public int getItemCount() {
        return ratingQuestionsList.size();
    }


    class DataClass extends RecyclerView.ViewHolder implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

        @BindView(R.id.questionDesc)
        TextView questionDesc;
        @BindView(R.id.rating)
        RatingBar rating;

        DataClass(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rating.setOnRatingBarChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            currentPosition = getAdapterPosition();
            if (v.getId() == R.id.mainLinear) {
//                Intent intent = new Intent(activity, OrderDetailActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.putExtra(MyConstants.ORDERS, productList.get(currentPosition));
//                intent.putExtra(MyConstants.TYPE, MyConstants.ORDER_DATA);
//                activity.startActivity(intent);
//                activity.overridePendingTransition(0, 0);
            }
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            currentPosition = getAdapterPosition();
            CommonMethods.showLog(TAG, "AD POSITION : " + currentPosition + " Rating : " + rating);
            ratingQuestionsList.get(currentPosition).setRating(rating);
        }
    }
}
