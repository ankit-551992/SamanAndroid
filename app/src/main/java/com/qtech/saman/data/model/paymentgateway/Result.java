
package com.qtech.saman.data.model.paymentgateway;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Result {

    @Expose
    private Card card;
    @Expose
    private Message message;
    @Expose
    private String status;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
