package com.algorepublic.saman.data.model.apis;

import com.algorepublic.saman.data.model.StoreCategory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetCategoriesList{
    @SerializedName("result")
    @Expose
    private List<StoreCategory> categories = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<StoreCategory> getCategories() {
        return categories;
    }

    public void setResult(List<StoreCategory> categories) {
        this.categories = categories;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
