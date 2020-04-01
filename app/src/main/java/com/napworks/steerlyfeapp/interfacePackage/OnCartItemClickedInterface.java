package com.napworks.steerlyfeapp.interfacePackage;


import com.napworks.steerlyfeapp.modelPackage.ProductDetailModel;
import com.napworks.steerlyfeapp.modelPackage.QuizQuestionsModel;

import java.util.List;

public interface OnCartItemClickedInterface {
    public void onCartItemClicked(List<ProductDetailModel> productList, int position);
}
