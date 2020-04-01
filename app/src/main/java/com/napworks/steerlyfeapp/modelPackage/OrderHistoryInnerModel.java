package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryInnerModel implements Serializable {

    private String order_id;
    private String user_id;
    private double total_amount;
    private double discount_amount;
    private double order_rating;
    private double amount_paid;
    private boolean coupon_used;
    private String coupon_name;
    private double coupon_discount;
    private double total_savings;
    private long order_time;
    private AddressInnerModel address_detail;
    private String order_status;
    private boolean isLoadingType;
    private List<OrderDetailModel> order_info = new ArrayList<>();


    public OrderHistoryInnerModel(String order_id, String user_id, double total_amount, double discount_amount, double order_rating,
                                  double amount_paid, boolean coupon_used, String coupon_name, double coupon_discount,
                                  double total_savings, long order_time, AddressInnerModel address_detail, String order_status,
                                  boolean isLoadingType, List<OrderDetailModel> order_info) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.total_amount = total_amount;
        this.discount_amount = discount_amount;
        this.order_rating = order_rating;
        this.amount_paid = amount_paid;
        this.coupon_used = coupon_used;
        this.coupon_name = coupon_name;
        this.coupon_discount = coupon_discount;
        this.total_savings = total_savings;
        this.order_time = order_time;
        this.address_detail = address_detail;
        this.order_status = order_status;
        this.isLoadingType = isLoadingType;
        this.order_info = order_info;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public double getOrder_rating() {
        return order_rating;
    }

    public void setOrder_rating(double order_rating) {
        this.order_rating = order_rating;
    }

    public double getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(double amount_paid) {
        this.amount_paid = amount_paid;
    }

    public boolean isCoupon_used() {
        return coupon_used;
    }

    public void setCoupon_used(boolean coupon_used) {
        this.coupon_used = coupon_used;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public double getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(double coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public double getTotal_savings() {
        return total_savings;
    }

    public void setTotal_savings(double total_savings) {
        this.total_savings = total_savings;
    }

    public long getOrder_time() {
        return order_time;
    }

    public void setOrder_time(long order_time) {
        this.order_time = order_time;
    }

    public AddressInnerModel getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(AddressInnerModel address_detail) {
        this.address_detail = address_detail;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public boolean isLoadingType() {
        return isLoadingType;
    }

    public void setLoadingType(boolean loadingType) {
        isLoadingType = loadingType;
    }

    public List<OrderDetailModel> getOrder_info() {
        return order_info;
    }

    public void setOrder_info(List<OrderDetailModel> order_info) {
        this.order_info = order_info;
    }
}
