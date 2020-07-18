package com.example.androidjava.Model;

import java.util.List;

public class mDriverOrderCompleted {
    private String id;
    private String order_id;
    private List<mOrderItem> orderItem;
    private List<mRating> driver_reviews;
    private String totalKilometer;
    private String totalAmount;
    private String finishedTime;
    private String orderStatus;
    public mDriverOrderCompleted(String id, String order_id, List<mOrderItem> orderItem, List<mRating> driver_reviews, String totalKilometer) {
        this.id = id;
        this.order_id = order_id;
        this.orderItem = orderItem;
        this.driver_reviews = driver_reviews;
        this.totalKilometer = totalKilometer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<mOrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<mOrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    public List<mRating> getDriver_reviews() {
        return driver_reviews;
    }

    public void setDriver_reviews(List<mRating> driver_reviews) {
        this.driver_reviews = driver_reviews;
    }

    public String getTotalKilometer() {
        return totalKilometer;
    }

    public void setTotalKilometer(String totalKilometer) {
        this.totalKilometer = totalKilometer;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
