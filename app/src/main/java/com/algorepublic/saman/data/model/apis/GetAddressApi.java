package com.algorepublic.saman.data.model.apis;

import com.algorepublic.saman.data.model.ShippingAddress;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAddressApi{

    @SerializedName("result")
    @Expose
    private List<ShippingAddress> result = null;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ShippingAddress> getResult() {
        return result;
    }

    public void setResult(List<ShippingAddress> result) {
        this.result = result;
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
