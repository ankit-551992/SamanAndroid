package com.algorepublic.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShippingAddress implements Serializable {


    @SerializedName("AddressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("AddressLine2")
    @Expose
    private Object addressLine2;
    @SerializedName("City")
    @Expose
    private Object city;
    @SerializedName("State")
    @Expose
    private Object state;
    @SerializedName("ZipCode")
    @Expose
    private Object zipCode;
    @SerializedName("Country")
    @Expose
    private Object country;
    @SerializedName("isDefault")
    @Expose
    private boolean isDefault;
    @SerializedName("ID")
    @Expose
    private Integer iD;

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public Object getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(Object addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getZipCode() {
        return zipCode;
    }

    public void setZipCode(Object zipCode) {
        this.zipCode = zipCode;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
