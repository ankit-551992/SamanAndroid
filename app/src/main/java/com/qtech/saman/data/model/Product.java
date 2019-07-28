package com.qtech.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    int cartID;
    int cartCategory;
    int cartAttributeID;
    int cartAttributeGroupID;
    int colorID;
    int availableQuantity;

    String optionValues;
    String options;
    String optionsAR;

    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductName_AR")
    @Expose
    private String productNameAR;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Description_AR")
    @Expose
    private String descriptionAR;
    @SerializedName("StoreName")
    @Expose
    private String storeName;
    @SerializedName("StoreName_AR")
    @Expose
    private String storeNameAR;
    @SerializedName("Price")
    @Expose
    private Float price;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("SizeLength")
    @Expose
    private Integer sizeLength;
    @SerializedName("SizeWidth")
    @Expose
    private Integer sizeWidth;
    @SerializedName("SizeHeight")
    @Expose
    private Integer sizeHeight;
    @SerializedName("Pictures")
    @Expose
    private List<String> pictures = null;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("isFavorite")
    @Expose
    private Boolean isFavorite;
    @SerializedName("ProductImagesURLs")
    @Expose
    private List<String> productImagesURLs = null;
    @SerializedName("LogoURL")
    @Expose
    private String LogoURL = null;
    @SerializedName("ProductCategories")
    @Expose
    private List<ProductCategory> productCategories = null;
    @SerializedName("ProductAttributes")
    @Expose
    private List<ProductAttribute> productAttributes = null;
    @SerializedName("ProductOption")
    @Expose
    private List<ProductOption> productOptions = null;
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

    public String getDescription() {
        if(description==null){
            description="";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionAR() {
        if(descriptionAR==null){
            descriptionAR="null";
        }
        return descriptionAR;
    }

    public void setDescriptionAR(String descriptionAR) {
        this.descriptionAR = descriptionAR;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSizeLength() {
        return sizeLength;
    }

    public void setSizeLength(Integer sizeLength) {
        this.sizeLength = sizeLength;
    }

    public Integer getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(Integer sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    public Integer getSizeHeight() {
        return sizeHeight;
    }

    public void setSizeHeight(Integer sizeHeight) {
        this.sizeHeight = sizeHeight;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<String> getProductImagesURLs() {
        return productImagesURLs;
    }

    public void setProductImagesURLs(List<String> productImagesURLs) {
        this.productImagesURLs = productImagesURLs;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
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

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getCartCategory() {
        return cartCategory;
    }

    public void setCartCategory(int cartCategory) {
        this.cartCategory = cartCategory;
    }

    public int getCartAttributeID() {
        return cartAttributeID;
    }

    public void setCartAttributeID(int cartAttributeID) {
        this.cartAttributeID = cartAttributeID;
    }

    public int getCartAttributeGroupID() {
        return cartAttributeGroupID;
    }

    public void setCartAttributeGroupID(int cartAttributeGroupID) {
        this.cartAttributeGroupID = cartAttributeGroupID;
    }

    public int getColorID() {
        return colorID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public Boolean getFavorite() {
        if(isFavorite==null){
            isFavorite=false;
        }
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public List<ProductOption> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductOption> productOptions) {
        this.productOptions = productOptions;
    }

    public String getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(String optionValues) {
        this.optionValues = optionValues;
    }

    public String getLogoURL() {
        return LogoURL;
    }

    public void setLogoURL(String logoURL) {
        LogoURL = logoURL;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }


    public String getOptionsAR() {
        return optionsAR;
    }

    public void setOptionsAR(String optionsAR) {
        this.optionsAR = optionsAR;
    }

    public String getStoreName() {
        if(storeName==null){
            storeName="";
        }
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreNameAR() {
        if(storeNameAR==null){
            storeNameAR="";
        }
        return storeNameAR;
    }

    public void setStoreNameAR(String storeNameAR) {
        this.storeNameAR = storeNameAR;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
