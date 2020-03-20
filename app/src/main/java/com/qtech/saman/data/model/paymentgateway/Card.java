
package com.qtech.saman.data.model.paymentgateway;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Card {

    @SerializedName("CVV")
    private String cVV;
    @SerializedName("CardHolderName")
    private String cardHolderName;
    @SerializedName("CreditCardNumber")
    private String creditCardNumber;
    @SerializedName("ExpiryMonth")
    private String expiryMonth;
    @SerializedName("ExpiryYear")
    private String expiryYear;

    public String getCVV() {
        return cVV;
    }

    public void setCVV(String cVV) {
        this.cVV = cVV;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

}
