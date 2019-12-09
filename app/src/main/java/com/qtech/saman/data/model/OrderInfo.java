package com.qtech.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderInfo implements Serializable {

    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Status")
    @Expose
    private String OrderStatus;
    @SerializedName("OrderNumber")
    @Expose
    private String OrderNumber;
    @SerializedName("DeliveryDate")
    @Expose
    private String DeliveryDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "id=" + id +
                ", OrderStatus='" + OrderStatus + '\'' +
                ", OrderNumber='" + OrderNumber + '\'' +
                ", DeliveryDate='" + DeliveryDate + '\'' +
                '}';
    }
}
