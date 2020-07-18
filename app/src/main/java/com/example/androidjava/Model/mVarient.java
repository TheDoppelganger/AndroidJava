package com.example.androidjava.Model;

public class mVarient {
    private String productVarient;
    private String productMrp;
    private String productPrice;
    private String proudctPacked;

    public mVarient() {
    }

    public mVarient(String productVarient, String productMrp, String productPrice, String proudctPacked) {
        this.productVarient = productVarient;
        this.productMrp = productMrp;
        this.productPrice = productPrice;
        this.proudctPacked = proudctPacked;
    }

    public String getProductVarient() {
        return productVarient;
    }

    public void setProductVarient(String productVarient) {
        this.productVarient = productVarient;
    }

    public String getProductMrp() {
        return productMrp;
    }

    public void setProductMrp(String productMrp) {
        this.productMrp = productMrp;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProudctPacked() {
        return proudctPacked;
    }

    public void setProudctPacked(String proudctPacked) {
        this.proudctPacked = proudctPacked;
    }


}
