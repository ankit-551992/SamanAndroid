package com.algorepublic.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCategory {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Title_AR")
    @Expose
    private String titleAR;
    @SerializedName("SortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("LogoURL")
    @Expose
    private Object logoURL;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Object getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(Object logoURL) {
        this.logoURL = logoURL;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }
}
