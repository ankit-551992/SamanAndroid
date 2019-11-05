package com.qtech.saman.data.model.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.qtech.saman.data.model.ShippingUpdateAddress;

import java.util.List;

public class GetAddressApi{

    @SerializedName("result")
    @Expose
    private List<ShippingUpdateAddress> result = null;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ShippingUpdateAddress> getResult() {
        return result;
    }

    public void setResult(List<ShippingUpdateAddress> result) {
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
