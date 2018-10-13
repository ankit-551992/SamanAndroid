package com.algorepublic.saman.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("apiToken")
    @Expose
    private Object apiToken;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("phoneNumber")
    @Expose
    private Object phoneNumber;
    @SerializedName("languageID")
    @Expose
    private Integer languageID;
    @SerializedName("profilePictureID")
    @Expose
    private Integer profilePictureID;
    @SerializedName("roleID")
    @Expose
    private Integer roleID;
    @SerializedName("profileImagePath")
    @Expose
    private String profileImagePath;
    @SerializedName("country")
    @Expose
    private Object country;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("devicetoken")
    @Expose
    private Object devicetoken;
    @SerializedName("deviceType")
    @Expose
    private Object deviceType;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createBy")
    @Expose
    private Integer createBy;
    @SerializedName("updateBy")
    @Expose
    private Integer updateBy;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Object getApiToken() {
        return apiToken;
    }

    public void setApiToken(Object apiToken) {
        this.apiToken = apiToken;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getLanguageID() {
        return languageID;
    }

    public void setLanguageID(Integer languageID) {
        this.languageID = languageID;
    }

    public Integer getProfilePictureID() {
        return profilePictureID;
    }

    public void setProfilePictureID(Integer profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(Object devicetoken) {
        this.devicetoken = devicetoken;
    }

    public Object getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Object deviceType) {
        this.deviceType = deviceType;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
