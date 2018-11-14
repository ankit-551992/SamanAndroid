package com.algorepublic.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OptionValue {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Title_AR")
    @Expose
    private String titleAR;
    @SerializedName("SortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("OptionID")
    @Expose
    private Integer optionID;
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

    public Integer getOptionID() {
        return optionID;
    }

    public void setOptionID(Integer optionID) {
        this.optionID = optionID;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    @Override
    public String toString() {
        return title;
    }
}
