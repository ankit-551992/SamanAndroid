package com.algorepublic.saman.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class CardDs implements Serializable {

    @SerializedName("cardHolder")
    String cardHolder;

    @SerializedName("cardNumber")
    String cardNumber;

    @SerializedName("expireDate")
    String expireDate;

    @SerializedName("cvc")
    String cvc;


    @SerializedName("otp")
    String otp;

    @SerializedName("month")
    int month;

    @SerializedName("year")
    int year;


    public CardDs() {

    }

    public CardDs(String cardHolder, String cardNumber, String expireDate, String cvc,String otp, int month, int year) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expireDate = expireDate;
        this.cvc = cvc;
        this.otp = otp;
        this.month = month;
        this.year = year;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getCvc() {
        return cvc;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}


