package com.qtech.saman.data.model.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.qtech.saman.data.model.OrderHistory;

import java.util.List;

public class OrderHistoryAPI {

    @SerializedName("result")
    @Expose
    private List<OrderHistory> result = null;

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<OrderHistory> getResult() {
        return result;
    }

    public void setResult(List<OrderHistory> result) {
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
