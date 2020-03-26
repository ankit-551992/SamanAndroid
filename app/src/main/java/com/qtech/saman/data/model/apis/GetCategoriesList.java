package com.qtech.saman.data.model.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.qtech.saman.data.model.StoreCategory;

import java.util.List;

public class GetCategoriesList{
    @SerializedName("result")
    @Expose
    private List<StoreCategory> categories = null;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<StoreCategory> getCategories() {
        return categories;
    }

    public void setResult(List<StoreCategory> categories) {
        this.categories = categories;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        if(message==null){
            message="Server Error";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
