package com.napworks.steerlyfeapp.interfacePackage;


import com.napworks.steerlyfeapp.modelPackage.RatingInnerModel;
import com.napworks.steerlyfeapp.modelPackage.RatingQuestionsModel;

import java.util.List;

public interface OnRateProductItemClickedInterFace {

    void onRateProductItemClicked(int adapterPosition, List<RatingQuestionsModel> ratingList, RatingInnerModel ratingInnerModel);
}
