package com.napworks.steerlyfeapp.interfacePackage;


import com.napworks.steerlyfeapp.modelPackage.QuizQuestionsModel;
import com.napworks.steerlyfeapp.modelPackage.SortingModel;

import java.util.List;

public interface OnFilterDialogInterface {
    public void OnFilterDialogSelected(List<SortingModel> sortingList, int position);
}
