package com.qtech.saman.data.model;

public class Order {


    private String name;
    private String date;


    public Order(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public Order() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
