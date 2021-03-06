package com.qtech.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductAttribute {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Title_AR")
    @Expose
    private String titleAR;
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("Value_AR")
    @Expose
    private String valueAR;
    @SerializedName("SortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("AttributeGroupID")
    @Expose
    private Integer attributeGroupID;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("ID")
    @Expose
    private Integer iD;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAR() {
        return titleAR;
    }

    public void setTitleAR(String titleAR) {
        this.titleAR = titleAR;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getAttributeGroupID() {
        return attributeGroupID;
    }

    public void setAttributeGroupID(Integer attributeGroupID) {
        this.attributeGroupID = attributeGroupID;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueAR() {
        return valueAR;
    }

    public void setValueAR(String valueAR) {
        this.valueAR = valueAR;
    }

    @Override
    public String toString() {
        return title;
    }

}
