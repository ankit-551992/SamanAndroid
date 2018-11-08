package com.algorepublic.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistory {


    @SerializedName("OrderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("PaymentType")
    @Expose
    private String paymentType;
    @SerializedName("DeliveryDate")
    @Expose
    private String deliveryDate;
    @SerializedName("CustomerID")
    @Expose
    private Integer customerID;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("ShippingAddress")
    @Expose
    private Object shippingAddress;
    @SerializedName("BillingAddress")
    @Expose
    private Object billingAddress;
    @SerializedName("ShippingTotal")
    @Expose
    private Integer shippingTotal;
    @SerializedName("TotalPrice")
    @Expose
    private Integer totalPrice;
    @SerializedName("DiscountCoupan")
    @Expose
    private String discountCoupan;
    @SerializedName("OrderItems")
    @Expose
    private List<OrderItem> orderItems = null;
    @SerializedName("CreatedAt")
    @Expose
    private String createdAt;
    @SerializedName("UpdatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("CreateBy")
    @Expose
    private Integer createBy;
    @SerializedName("UpdateBy")
    @Expose
    private Integer updateBy;
    @SerializedName("IsDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("ID")
    @Expose
    private Integer iD;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Object getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Object shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Object getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Object billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Integer getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(Integer shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDiscountCoupan() {
        return discountCoupan;
    }

    public void setDiscountCoupan(String discountCoupan) {
        this.discountCoupan = discountCoupan;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }


}
