package com.qtech.saman.data.model.apis;

import com.qtech.saman.data.model.Coupon;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoVerify {


    @SerializedName("result")
    @Expose
    private Coupon result;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public Coupon getResult() {
        return result;
    }

    public void setResult(Coupon result) {
        this.result = result;
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
