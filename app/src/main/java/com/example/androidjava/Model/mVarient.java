package com.example.androidjava.Model;

public class mVarient {
    private String productVarient;
    private String productMrp;
    private String productPrice;
    private String proudctPacked;
    private String productUnit;
    private String productStock;
    private String productBarCode;

    public mVarient() {
    }

    public mVarient(String productVarient, String productMrp, String productPrice, String proudctPacked, String productUnit, String productStock, String productBarCode) {
        this.productVarient = productVarient;
        this.productMrp = productMrp;
        this.productPrice = productPrice;
        this.proudctPacked = proudctPacked;
        this.productUnit = productUnit;
        this.productStock = productStock;
        this.productBarCode = productBarCode;
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

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductStock() {
        return productStock;
    }

    public void setProductStock(String productStock) {
        this.productStock = productStock;
    }

    public String getProductBarCode() {
        return productBarCode;
    }

    public void setProductBarCode(String productBarCode) {
        this.productBarCode = productBarCode;
    }
}
