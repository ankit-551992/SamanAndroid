package com.qtech.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryObject {

    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("FlagURL")
    @Expose
    private String flagURL;
    @SerializedName("CountryName")
    @Expose
    private String countryName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFlagURL() {
        return flagURL;
    }

    public void setFlagURL(String flagURL) {
        this.flagURL = flagURL;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
