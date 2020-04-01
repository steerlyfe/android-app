package com.napworks.steerlyfeapp.modelPackage;

public class ReviewsModel {

    private String review_id;
    private String user_id;
    private String full_name;
    private int rating;
    private long rating_time;
    private String description;
    private String image_url;


    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getRating_time() {
        return rating_time;
    }

    public void setRating_time(long rating_time) {
        this.rating_time = rating_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
