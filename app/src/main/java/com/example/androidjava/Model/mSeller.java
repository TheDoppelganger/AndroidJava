package com.example.androidjava.Model;

public class mSeller {
    private String id;
    private String shop_name;
    private String shop_pincode;
    private String shop_latitude;
    private String shop_longitude;
    private String shop_front_image;
    private String shop_inventory_image;
    private String shop_category;
    private String shop_contact_number;
    private String shop_contact_email;
    private String shop_gst_number;
    private String shop_owner_id;
    private String isOpen;
    private String shop_rate;
    private String shopTotalOrder;
    private String shopTotalReturnOrder;
    private String shopTotalSell;
    private String shopLowInventory;

    public mSeller(String id, String shop_name, String shop_pincode, String shop_latitude, String shop_longitude, String shop_front_image, String shop_inventory_image, String shop_category, String shop_contact_number, String shop_contact_email, String shop_gst_number) {
        this.id = id;
        this.shop_name = shop_name;
        this.shop_pincode = shop_pincode;
        this.shop_latitude = shop_latitude;
        this.shop_longitude = shop_longitude;
        this.shop_front_image = shop_front_image;
        this.shop_inventory_image = shop_inventory_image;
        this.shop_category = shop_category;
        this.shop_contact_number = shop_contact_number;
        this.shop_contact_email = shop_contact_email;
        this.shop_gst_number = shop_gst_number;
    }

    public mSeller() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_pincode() {
        return shop_pincode;
    }

    public void setShop_pincode(String shop_pincode) {
        this.shop_pincode = shop_pincode;
    }

    public String getShop_latitude() {
        return shop_latitude;
    }

    public void setShop_latitude(String shop_latitude) {
        this.shop_latitude = shop_latitude;
    }

    public String getShop_longitude() {
        return shop_longitude;
    }

    public void setShop_longitude(String shop_longitude) {
        this.shop_longitude = shop_longitude;
    }

    public String getShop_front_image() {
        return shop_front_image;
    }

    public void setShop_front_image(String shop_front_image) {
        this.shop_front_image = shop_front_image;
    }

    public String getShop_inventory_image() {
        return shop_inventory_image;
    }

    public void setShop_inventory_image(String shop_inventory_image) {
        this.shop_inventory_image = shop_inventory_image;
    }

    public String getShop_category() {
        return shop_category;
    }

    public void setShop_category(String shop_category) {
        this.shop_category = shop_category;
    }

    public String getShop_contact_number() {
        return shop_contact_number;
    }

    public void setShop_contact_number(String shop_contact_number) {
        this.shop_contact_number = shop_contact_number;
    }

    public String getShop_contact_email() {
        return shop_contact_email;
    }

    public void setShop_contact_email(String shop_contact_email) {
        this.shop_contact_email = shop_contact_email;
    }

    public String getShop_gst_number() {
        return shop_gst_number;
    }

    public void setShop_gst_number(String shop_gst_number) {
        this.shop_gst_number = shop_gst_number;
    }

    public String getShop_owner_id() {
        return shop_owner_id;
    }

    public void setShop_owner_id(String shop_owner_id) {
        this.shop_owner_id = shop_owner_id;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getShop_rate() {
        return shop_rate;
    }

    public void setShop_rate(String shop_rate) {
        this.shop_rate = shop_rate;
    }

    public String getShopTotalOrder() {
        return shopTotalOrder;
    }

    public void setShopTotalOrder(String shopTotalOrder) {
        this.shopTotalOrder = shopTotalOrder;
    }

    public String getShopTotalReturnOrder() {
        return shopTotalReturnOrder;
    }

    public void setShopTotalReturnOrder(String shopTotalReturnOrder) {
        this.shopTotalReturnOrder = shopTotalReturnOrder;
    }

    public String getShopTotalSell() {
        return shopTotalSell;
    }

    public void setShopTotalSell(String shopTotalSell) {
        this.shopTotalSell = shopTotalSell;
    }

    public String getShopLowInventory() {
        return shopLowInventory;
    }

    public void setShopLowInventory(String shopLowInventory) {
        this.shopLowInventory = shopLowInventory;
    }
}
