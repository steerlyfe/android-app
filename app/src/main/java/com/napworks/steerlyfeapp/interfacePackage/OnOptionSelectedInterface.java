package com.napworks.steerlyfeapp.interfacePackage;


import com.napworks.steerlyfeapp.modelPackage.QuizQuestionsModel;

import java.util.List;

public interface OnOptionSelectedInterface {
    public void OnOptionSelected(List<QuizQuestionsModel> optionsList, int position);
}
