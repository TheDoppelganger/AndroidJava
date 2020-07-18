package com.example.androidjava.Model;

import java.util.List;

public class mOrderCustomerOnline {
    private String id;
    private String order_no;
    private String order_amount;
    private String payment_mode;
    private String order_date;
    private String total_payable_amount;
    private String order_status;
    private List<mOrderItem> order_item;
    private String iSReviewGive;
    private String driver_id;
    private String driver_name;
    private List<mSeller> sellerList;

    public mOrderCustomerOnline() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getTotal_payable_amount() {
        return total_payable_amount;
    }

    public void setTotal_payable_amount(String total_payable_amount) {
        this.total_payable_amount = total_payable_amount;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public List<mOrderItem> getOrder_item() {
        return order_item;
    }

    public void setOrder_item(List<mOrderItem> order_item) {
        this.order_item = order_item;
    }

    public String getiSReviewGive() {
        return iSReviewGive;
    }

    public void setiSReviewGive(String iSReviewGive) {
        this.iSReviewGive = iSReviewGive;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public List<mSeller> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<mSeller> sellerList) {
        this.sellerList = sellerList;
    }
}
