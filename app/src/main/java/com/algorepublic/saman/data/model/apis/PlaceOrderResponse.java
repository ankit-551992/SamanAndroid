package com.algorepublic.saman.data.model.apis;

import com.algorepublic.saman.data.model.OrderInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlaceOrderResponse implements Serializable{

    @SerializedName("result")
    @Expose
    private OrderInfo result;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

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

    public OrderInfo getResult() {
        return result;
    }

    public void setResult(OrderInfo result) {
        this.result = result;
    }
}
