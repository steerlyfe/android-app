package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoriesMainModel implements Serializable {

    private String status;
    private String message;
    private int perPageItems;
    private int totalProducts;
    public List<ProductDetailModel> productList = new ArrayList<>();

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

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }
}
