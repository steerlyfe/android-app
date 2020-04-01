package com.napworks.steerlyfeapp.modelPackage;

import java.util.ArrayList;
import java.util.List;

public class CouponsListMainModel {

    private String status;
    private String message;
    private List<CouponListInnerModel> couponList = new ArrayList<>();

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

    public List<CouponListInnerModel> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponListInnerModel> couponList) {
        this.couponList = couponList;
    }
}
