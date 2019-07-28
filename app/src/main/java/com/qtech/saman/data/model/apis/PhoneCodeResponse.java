package com.qtech.saman.data.model.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneCodeResponse {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
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
