package com.napworks.steerlyfeapp.modelPackage;

import java.util.ArrayList;
import java.util.List;

public class RatingInnerModel {


    private String question_id;
    private String question;
    private String message;
    private float rating=0f;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
