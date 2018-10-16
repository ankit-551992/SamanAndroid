package com.algorepublic.saman.ui.activities.myaccount.myorders;

import com.algorepublic.saman.data.model.Order;

public class GeneralItem extends ListItem {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }

}
