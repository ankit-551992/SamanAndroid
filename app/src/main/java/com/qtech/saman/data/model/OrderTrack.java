package com.qtech.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderTrack implements Serializable {

    @SerializedName("OrderStatusID")
    @Expose
    private Integer orderStatusID;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Comment")
    @Expose
    private String comment;
    @SerializedName("Comment_AR")
    @Expose
    private String commentAR;
    @SerializedName("CreatedAt")
    @Expose
    private String date;
    @SerializedName("OrderID")
    @Expose
    private Integer orderID;
    @SerializedName("ID")
    @Expose
    private Integer iD;

    private String productName;
    private String productNameAR;

    public Integer getOrderStatusID() {
        return orderStatusID;
    }

    public void setOrderStatusID(Integer orderStatusID) {
        this.orderStatusID = orderStatusID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentAR() {
        return commentAR;
    }

    public void setCommentAR(String commentAR) {
        this.commentAR = commentAR;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameAR() {
        return productNameAR;
    }

    public void setProductNameAR(String productNameAR) {
        this.productNameAR = productNameAR;
    }
}
