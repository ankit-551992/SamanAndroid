package com.qtech.saman.ui.activities.myaccount.myorders;

public abstract class ListItem {

    public static final int TYPE_PARENT = 0;
    public static final int TYPE_GENERAL = 1;

    abstract public int getType();
}
