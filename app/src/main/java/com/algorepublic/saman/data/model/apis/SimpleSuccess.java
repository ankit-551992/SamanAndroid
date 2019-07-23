package com.algorepublic.saman.data.model.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleSuccess {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
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

    @Override
    public String toString() {
        return "SimpleSuccess{" +
                "result=" + result +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
