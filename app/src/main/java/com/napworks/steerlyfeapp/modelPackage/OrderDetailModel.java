package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailModel implements Serializable {

    private String product_id;
    private String product_name;
    private String product_image;
    private double actual_price;
    private double sale_price;
    private String store_id;
    private String store_name;
    private String sub_store_id;
    private String sub_store_address;
    private String product_availability;
    private double product_availability_price;
    private String additional_feature;
    private double additional_feature_price;
    private int quantity;
    private int product_quantity;
    private long deliveredDate;
    private List<RatingQuestionsModel> questions = new ArrayList<>();

    private OrderStatusModel status = new OrderStatusModel();

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public double getActual_price() {
        return actual_price;
    }

    public void setActual_price(double actual_price) {
        this.actual_price = actual_price;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getSub_store_id() {
        return sub_store_id;
    }

    public void setSub_store_id(String sub_store_id) {
        this.sub_store_id = sub_store_id;
    }

    public String getSub_store_address() {
        return sub_store_address;
    }

    public void setSub_store_address(String sub_store_address) {
        this.sub_store_address = sub_store_address;
    }

    public String getProduct_availability() {
        return product_availability;
    }

    public void setProduct_availability(String product_availability) {
        this.product_availability = product_availability;
    }

    public double getProduct_availability_price() {
        return product_availability_price;
    }

    public void setProduct_availability_price(double product_availability_price) {
        this.product_availability_price = product_availability_price;
    }

    public String getAdditional_feature() {
        return additional_feature;
    }

    public void setAdditional_feature(String additional_feature) {
        this.additional_feature = additional_feature;
    }

    public double getAdditional_feature_price() {
        return additional_feature_price;
    }

    public void setAdditional_feature_price(double additional_feature_price) {
        this.additional_feature_price = additional_feature_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public OrderStatusModel getStatus() {
        return status;
    }

    public void setStatus(OrderStatusModel status) {
        this.status = status;
    }

    public long getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(long deliveredDate) {
        this.deliveredDate = deliveredDate;
    }
    public List<RatingQuestionsModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<RatingQuestionsModel> questions) {
        this.questions = questions;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }
}
