package com.napworks.steerlyfeapp.modelPackage;

public class RatingQuestionsModel {

    private String question_id = "";
    private String type = "";
    private String question = "";
    private float rating = 0f;
    private int adapterPosition = 0;

    public RatingQuestionsModel(String question_id, float rating, int adapterPosition, String question) {
        this.question_id = question_id;
        this.rating = rating;
        this.question = question;
        this.adapterPosition = adapterPosition;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }
}
