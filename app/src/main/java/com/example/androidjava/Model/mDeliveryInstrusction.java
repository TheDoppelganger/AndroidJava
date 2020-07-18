package com.example.androidjava.Model;

import java.util.ArrayList;

public class mDeliveryInstrusction {
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private String shopOrCustomer;
    private String shopFrontImage;
    private String contactNumber;
    private String directionShow;
    private ArrayList<mOrderItem> shopItem;

    public mDeliveryInstrusction(String name, String address, String latitude, String longitude, String shopOrCustomer, String shopFrontImage, String contactNumber, String directionShow) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.shopOrCustomer = shopOrCustomer;
        this.shopFrontImage = shopFrontImage;
        this.contactNumber = contactNumber;
        this.directionShow = directionShow;

    }

    public mDeliveryInstrusction() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getShopOrCustomer() {
        return shopOrCustomer;
    }

    public void setShopOrCustomer(String shopOrCustomer) {
        this.shopOrCustomer = shopOrCustomer;
    }

    public String getShopFrontImage() {
        return shopFrontImage;
    }

    public void setShopFrontImage(String shopFrontImage) {
        this.shopFrontImage = shopFrontImage;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDirectionShow() {
        return directionShow;
    }

    public void setDirectionShow(String directionShow) {
        this.directionShow = directionShow;
    }

    public ArrayList<mOrderItem> getShopItem() {
        return shopItem;
    }

    public void setShopItem(ArrayList<mOrderItem> shopItem) {
        this.shopItem = shopItem;
    }
}

