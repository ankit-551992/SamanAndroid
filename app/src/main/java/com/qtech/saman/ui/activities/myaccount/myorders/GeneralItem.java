package com.qtech.saman.ui.activities.myaccount.myorders;

import com.qtech.saman.data.model.OrderHistory;

public class GeneralItem extends ListItem {
    private OrderHistory order;

    public OrderHistory getOrder() {
        return order;
    }

    public void setOrder(OrderHistory order) {
        this.order = order;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }

}
