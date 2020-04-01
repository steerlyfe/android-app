package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryMainModel implements Serializable {

    private String status;
    private String message;
    private int perPageItems;
    private List<OrderHistoryInnerModel> orderList;
    private List<ProductDetailModel> productList;

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

    public List<OrderHistoryInnerModel> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderHistoryInnerModel> orderList) {
        this.orderList = orderList;
    }

    public List<ProductDetailModel> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDetailModel> productList) {
        this.productList = productList;
    }
}
