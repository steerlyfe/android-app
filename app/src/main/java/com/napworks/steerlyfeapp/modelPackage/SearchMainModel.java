package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchMainModel implements Serializable {
    private String status;
    private String message;
    private String errorData;
    private int perPageItems;
    public List<ProductDetailModel> productList = new ArrayList<>();
    private List<StoreDetailModel> storeList = new ArrayList<>();

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

    public int getPerPageItems() {
        return perPageItems;
    }

    public void setPerPageItems(int perPageItems) {
        this.perPageItems = perPageItems;
    }

    public List<ProductDetailModel> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDetailModel> productList) {
        this.productList = productList;
    }

    public List<StoreDetailModel> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreDetailModel> storeList) {
        this.storeList = storeList;
    }

    public String getErrorData() {
        return errorData;
    }

    public void setErrorData(String errorData) {
        this.errorData = errorData;
    }
}
