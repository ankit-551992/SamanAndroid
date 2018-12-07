package com.algorepublic.saman.data.model.apis;

import com.algorepublic.saman.data.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendMessageApi {

    @SerializedName("result")
    @Expose
    private Message result = null;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public Message getResult() {
        return result;
    }

    public void setResult(Message result) {
        this.result = result;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
