package com.algorepublic.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductOption {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Title_AR")
    @Expose
    private String titleAR;
    @SerializedName("SortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("Type")
    @Expose
    private Object type;
    @SerializedName("OptionValues")
    @Expose
    private List<OptionValue> optionValues = null;
    @SerializedName("OptionValue")
    @Expose
    private OptionValue optionValue = null;
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

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public List<OptionValue> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(List<OptionValue> optionValues) {
        this.optionValues = optionValues;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public OptionValue getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(OptionValue optionValue) {
        this.optionValue = optionValue;
    }
}
