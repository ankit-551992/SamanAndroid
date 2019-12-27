package com.qtech.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Slider {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Type")
    @Expose
    private Integer Type;
    @SerializedName("SliderURL")
    @Expose
    private String bannerURL = null;
    @SerializedName("SortOrder")
    @Expose
    private String sortOrder;
    @SerializedName("ProductID")
    @Expose
    private ArrayList<String> productIDlist;

    public Integer getiD() {
        return iD;
    }

    public void setiD(Integer iD) {
        this.iD = iD;
    }

    public String getBannerURL() {
        return bannerURL;
    }

    public void setBannerURL(String bannerURL) {
        this.bannerURL = bannerURL;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public ArrayList<String> getProductIDlist() {
        return productIDlist;
    }

    public void setProductIDlist(ArrayList<String> productIDlist) {
        this.productIDlist = productIDlist;
    }

    @Override
    public String toString() {
        return "Slider{" +
                "iD=" + iD +
                ", Type=" + Type +
                ", bannerURL='" + bannerURL + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", productIDlist=" + productIDlist +
                '}';
    }
}
