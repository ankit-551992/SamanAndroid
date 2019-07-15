package com.algorepublic.saman.ui.activities.myaccount.myorders;

public class DateItem extends ListItem {

    private String orderNumber;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public int getType() {
        return TYPE_PARENT;
    }
}
