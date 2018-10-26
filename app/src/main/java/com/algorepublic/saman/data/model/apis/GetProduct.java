package com.algorepublic.saman.data.model.apis;

import com.algorepublic.saman.data.model.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProduct{

    @SerializedName("result")
    @Expose
    private Product product;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
