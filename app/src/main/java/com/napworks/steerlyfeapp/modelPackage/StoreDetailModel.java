package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreDetailModel implements Serializable {

    private String subStoreId;
    private String storeId;
    private String storeLogo;
    private String storeName;
    private String bannerUrl;
    private float storeRating;
    private String description;
    private String address;
    private Double lat;
    private Double lng;
    private Double distance;
    private String phoneNumber;
    private String email;
    private String openingTime;
    private String closingTime;
    private List<ReviewsModel> reviews = new ArrayList<>();
    private boolean selected;
    private int ratingCount;
    private boolean userRated;
    private List<PostsInnerModel> postsList = new ArrayList<>();


    public String getSubStoreId() {
        return subStoreId;
    }

    public void setSubStoreId(String subStoreId) {
        this.subStoreId = subStoreId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public float getStoreRating() {
        return storeRating;
    }

    public void setStoreRating(float storeRating) {
        this.storeRating = storeRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public List<ReviewsModel> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewsModel> reviews) {
        this.reviews = reviews;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public boolean isUserRated() {
        return userRated;
    }

    public void setUserRated(boolean userRated) {
        this.userRated = userRated;
    }

    public List<PostsInnerModel> getPostsList() {
        return postsList;
    }

    public void setPostsList(List<PostsInnerModel> postsList) {
        this.postsList = postsList;
    }
}
