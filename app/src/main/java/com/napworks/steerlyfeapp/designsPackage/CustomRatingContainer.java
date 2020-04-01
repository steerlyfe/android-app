package com.napworks.steerlyfeapp.designsPackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.napworks.steerlyfeapp.R;
import com.napworks.steerlyfeapp.interfacePackage.OnRateProductItemClickedInterFace;
import com.napworks.steerlyfeapp.modelPackage.ProductInfoModel;
import com.napworks.steerlyfeapp.modelPackage.RatingInnerModel;
import com.napworks.steerlyfeapp.modelPackage.RatingQuestionsModel;
import com.napworks.steerlyfeapp.utilsPackage.CommonMethods;
import com.napworks.steerlyfeapp.utilsPackage.MyConstants;

import java.util.ArrayList;
import java.util.List;


public class CustomRatingContainer {

    private final Activity activity;

    private final LinearLayout inflatedProductInfoLayout;
    private final OnRateProductItemClickedInterFace onRateProductItemClickedInterFace;
    private final int adapterPosition;
    private final RatingInnerModel productDetailModel;

    private List<RatingQuestionsModel> ratingQuestionsList;
    private List<RatingQuestionsModel> localList = new ArrayList<>();
    private String TAG = getClass().getSimpleName();

    public CustomRatingContainer(int adapterPosition, Activity activity, List<RatingQuestionsModel> ratingQuestionsList,
                                 RatingInnerModel productDetailModel, LinearLayout inflatedProductInfoLayout, OnRateProductItemClickedInterFace onRateProductItemClickedInterFace) {
        this.activity = activity;
        this.ratingQuestionsList = ratingQuestionsList;
        this.inflatedProductInfoLayout = inflatedProductInfoLayout;
        this.adapterPosition = adapterPosition;
        this.productDetailModel = productDetailModel;
        this.onRateProductItemClickedInterFace = onRateProductItemClickedInterFace;
        setContainerDetail();
    }


    private void setContainerDetail() {
        for (int i = 0; i < ratingQuestionsList.size(); i++) {
            addProductInfoDataInContainer(i, ratingQuestionsList.get(i).getQuestion_id(), ratingQuestionsList.get(i).getQuestion(), ratingQuestionsList.get(i).getRating());
        }
    }


    private void addProductInfoDataInContainer(final int position, final String questionId, String question, float ratingValue) {
        if (!question.isEmpty()) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mainView = inflater.inflate(R.layout.container_rating, inflatedProductInfoLayout, false);
            TextView ratingTitle = mainView.findViewById(R.id.ratingTitle);
            RatingBar rating = mainView.findViewById(R.id.rating);
            rating.setRating(ratingValue);
            ratingTitle.setText(question.concat(" (*) "));


            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    CommonMethods.showLog(TAG, "Changed : " + rating + " at : " + position + " Adapter Pos : " + adapterPosition);
                    ratingQuestionsList.get(position).setRating(rating);
//                    if (localList.size() == 0) {
//                        localList.add(new RatingQuestionsModel(questionId, rating, adapterPosition, question));
//                    } else {
//                        for (int i = 0; i < localList.size(); i++) {
//                            if (questionId.equalsIgnoreCase(localList.get(i).getQuestion_id())) {
//                                localList.get(i).setRating(rating);
//                            } else {
//                                localList.add(new RatingQuestionsModel(questionId, rating, adapterPosition, question));
//                            }
//                        }
//                    }
//                    CommonMethods.showLog(TAG, "Local List Size " + localList.size());
                    onRateProductItemClickedInterFace.onRateProductItemClicked(adapterPosition, ratingQuestionsList, productDetailModel);
                }
            });


            inflatedProductInfoLayout.addView(mainView);
        }
    }
}
