package com.algorepublic.saman.data.model.apis;

import com.algorepublic.saman.data.model.HomeScreenData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeScreenAPI {

    @SerializedName("result")
    @Expose
    private HomeScreenData result;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public HomeScreenData getResult() {
        return result;
    }

    public void setResult(HomeScreenData result) {
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
