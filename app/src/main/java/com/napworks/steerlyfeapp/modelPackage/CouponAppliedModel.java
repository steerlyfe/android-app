package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;

public class CouponAppliedModel implements Serializable {

    private String status;
    private double payableAmountAfterDiscount;
    private String couponCountCheck;
    private String couponCode;
    private double discountAmount;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPayableAmountAfterDiscount() {
        return payableAmountAfterDiscount;
    }

    public void setPayableAmountAfterDiscount(double payableAmountAfterDiscount) {
        this.payableAmountAfterDiscount = payableAmountAfterDiscount;
    }

    public String getCouponCountCheck() {
        return couponCountCheck;
    }

    public void setCouponCountCheck(String couponCountCheck) {
        this.couponCountCheck = couponCountCheck;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
