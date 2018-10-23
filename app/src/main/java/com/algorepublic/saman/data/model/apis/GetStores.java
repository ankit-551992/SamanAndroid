package com.algorepublic.saman.data.model.apis;

import com.algorepublic.saman.data.model.Store;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetStores {

    @SerializedName("result")
    @Expose
    private List<Store> stores = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Store> getStores() {
        return stores;
    }

    public void setResult(List<Store> stores) {
        this.stores = stores;
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
