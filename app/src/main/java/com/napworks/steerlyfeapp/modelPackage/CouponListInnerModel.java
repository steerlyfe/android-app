package com.napworks.steerlyfeapp.modelPackage;

import java.io.Serializable;

public class CouponListInnerModel implements Serializable {

    public String couponId;
    public String couponCode;
    public String discountPercentage;
    public String discountPercentageUptoAmount;
    public long couponStartTime;
    public long couponEndTime;
    public long createdTime;
    public String coupon_type;
    public String discountType;
    public String current_status;
    public String usedbyCountOrEndDate;
    public int numberOfCoupon;
    public int totalUsedCoupon;
    public int discountAmount;
    public int moreThenAmount;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountPercentageUptoAmount() {
        return discountPercentageUptoAmount;
    }

    public void setDiscountPercentageUptoAmount(String discountPercentageUptoAmount) {
        this.discountPercentageUptoAmount = discountPercentageUptoAmount;
    }

    public long getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(long couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public long getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(long couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(String current_status) {
        this.current_status = current_status;
    }

    public String getUsedbyCountOrEndDate() {
        return usedbyCountOrEndDate;
    }

    public void setUsedbyCountOrEndDate(String usedbyCountOrEndDate) {
        this.usedbyCountOrEndDate = usedbyCountOrEndDate;
    }

    public int getNumberOfCoupon() {
        return numberOfCoupon;
    }

    public void setNumberOfCoupon(int numberOfCoupon) {
        this.numberOfCoupon = numberOfCoupon;
    }

    public int getTotalUsedCoupon() {
        return totalUsedCoupon;
    }

    public void setTotalUsedCoupon(int totalUsedCoupon) {
        this.totalUsedCoupon = totalUsedCoupon;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getMoreThenAmount() {
        return moreThenAmount;
    }

    public void setMoreThenAmount(int moreThenAmount) {
        this.moreThenAmount = moreThenAmount;
    }
}
