package com.algorepublic.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Coupon {

    @SerializedName("CouponCode")
    @Expose
    private String couponCode;
    @SerializedName("DiscountType")
    @Expose
    private Integer discountType;
    @SerializedName("Discount")
    @Expose
    private Integer discount;
    @SerializedName("CouponType")
    @Expose
    private Integer couponType;
    @SerializedName("ProductID")
    @Expose
    private List<Integer> productID = null;
    @SerializedName("StoreID")
    @Expose
    private List<Integer> storeID = null;
    @SerializedName("ID")
    @Expose
    private Integer iD;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public List<Integer> getProductID() {
        return productID;
    }

    public void setProductID(List<Integer> productID) {
        this.productID = productID;
    }

    public List<Integer> getStoreID() {
        return storeID;
    }

    public void setStoreID(List<Integer> storeID) {
        this.storeID = storeID;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

}
