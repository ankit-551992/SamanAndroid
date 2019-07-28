package com.qtech.saman.data.model.apis;

import com.qtech.saman.data.model.Conversation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetConversationsApi {

    @SerializedName("result")
    @Expose
    private List<Conversation> result = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Conversation> getResult() {
        return result;
    }

    public void setResult(List<Conversation> result) {
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
