package com.qtech.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sortname")
    @Expose
    private String sortname;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phoneCode")
    @Expose
    private String phoneCode;
    @SerializedName("name_AR")
    @Expose
    private String name_AR;

    String flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Country withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public Country withSortname(String sortname) {
        this.sortname = sortname;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country withName(String name) {
        this.name = name;
        return this;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Country withPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
        return this;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getName_AR() {
        return name_AR;
    }

    public void setName_AR(String name_AR) {
        this.name_AR = name_AR;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", sortname='" + sortname + '\'' +
                ", name='" + name + '\'' +
                ", phoneCode='" + phoneCode + '\'' +
                ", name_AR='" + name_AR + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
