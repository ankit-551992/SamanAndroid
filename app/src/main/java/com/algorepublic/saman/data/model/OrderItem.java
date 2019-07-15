package com.algorepublic.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderItem implements Serializable {


    @SerializedName("Product")
    @Expose
    private Product product;
    @SerializedName("ProductOption")
    @Expose
    private List<ProductOption> productOptionList = null;
    @SerializedName("isCancelled")
    @Expose
    private Boolean isCancelled;
    @SerializedName("ProductQuantity")
    @Expose
    private Integer productQuantity;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("OrderItemStatus")
    @Expose
    private List<OrderTrack> OrderTrackList = null;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ProductOption> getProductOptionList() {
        return productOptionList;
    }

    public void setProductOptionList(List<ProductOption> productOptionList) {
        this.productOptionList = productOptionList;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderTrack> getOrderTrackList() {
        return OrderTrackList;
    }

    public void setOrderTrackList(List<OrderTrack> orderTrackList) {
        OrderTrackList = orderTrackList;
    }
}
