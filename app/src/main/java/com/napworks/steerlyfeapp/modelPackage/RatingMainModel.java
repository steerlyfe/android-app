package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RatingMainModel implements Serializable {

    private String status;
    private String message;
    public List<RatingInnerModel> questionList = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RatingInnerModel> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<RatingInnerModel> questionList) {
        this.questionList = questionList;
    }
}
