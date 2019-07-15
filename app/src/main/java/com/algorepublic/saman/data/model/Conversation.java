package com.algorepublic.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Conversation {

    @SerializedName("Title")
    @Expose
    private String title;
//    @SerializedName("Title")
//    @Expose
    private String titleAr;
    @SerializedName("StoreName")
    @Expose
    private String storeName;
    @SerializedName("StoreName_AR")
    @Expose
    private String storeNameAR;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductName_AR")
    @Expose
    private String productNameAR;
    @SerializedName("Messages")
    @Expose
    private List<Message> messages = null;
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
    @SerializedName("RecipentID")
    @Expose
    private Integer RecipentID;
    @SerializedName("ImageURL")
    @Expose
    private String image;
    @SerializedName("ProductQuantity")
    @Expose
    private Integer ProductQuantity;
    @SerializedName("ProductPrice")
    @Expose
    private float ProductPrice;
    @SerializedName("TotalPrice")
    @Expose
    private float TotalPrice;
    @SerializedName("Status")
    @Expose
    private Integer status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNameAR() {
        return storeNameAR;
    }

    public void setStoreNameAR(String storeNameAR) {
        this.storeNameAR = storeNameAR;
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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

    public Integer getRecipentID() {
        return RecipentID;
    }

    public void setRecipentID(Integer recipentID) {
        RecipentID = recipentID;
    }

    public String getImage() {
        if(image==null){
            image="";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitleAr() {
        if(titleAr==null){
            titleAr=title;
        }
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public Integer getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        ProductQuantity = productQuantity;
    }

    public float getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(float productPrice) {
        ProductPrice = productPrice;
    }

    public float getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        TotalPrice = totalPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
