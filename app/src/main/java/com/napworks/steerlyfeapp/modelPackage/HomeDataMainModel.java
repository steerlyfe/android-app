package com.napworks.steerlyfeapp.modelPackage;

import java.util.ArrayList;
import java.util.List;

public class HomeDataMainModel {

    private String status;
    private String message;
    private String homeMessage;
    private List<ProductDetailModel> trendingProducts = new ArrayList<>();
    private List<ProductDetailModel> suggestedProducts = new ArrayList<>();


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

    public String getHomeMessage() {
        return homeMessage;
    }

    public void setHomeMessage(String homeMessage) {
        this.homeMessage = homeMessage;
    }

    public List<ProductDetailModel> getTrendingProducts() {
        return trendingProducts;
    }

    public void setTrendingProducts(List<ProductDetailModel> trendingProducts) {
        this.trendingProducts = trendingProducts;
    }

    public List<ProductDetailModel> getSuggestedProducts() {
        return suggestedProducts;
    }

    public void setSuggestedProducts(List<ProductDetailModel> suggestedProducts) {
        this.suggestedProducts = suggestedProducts;
    }
}
