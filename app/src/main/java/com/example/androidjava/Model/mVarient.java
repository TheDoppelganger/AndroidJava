package com.example.androidjava.Model;

public class mVarient {

    private String productMrp;
    private String productPrice;
    private String proudctPacked;
    private String productUnit;
    private String productStock;
    private String productBarCode;
    private String productVariantImage;
    private String productvariantImageName;
    public mVarient() {
    }

    public mVarient(String productMrp, String productPrice, String proudctPacked, String productUnit, String productStock, String productBarCode,String productvariantImageName) {
        this.productMrp = productMrp;
        this.productPrice = productPrice;
        this.proudctPacked = proudctPacked;
        this.productUnit = productUnit;
        this.productStock = productStock;
        this.productBarCode = productBarCode;
        this.productvariantImageName=productvariantImageName;
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

    public String getProductVariantImage() {
        return productVariantImage;
    }

    public void setProductVariantImage(String productVariantImage) {
        this.productVariantImage = productVariantImage;
    }

    public String getProductvariantImageName() {
        return productvariantImageName;
    }

    public void setProductvariantImageName(String productvariantImageName) {
        this.productvariantImageName = productvariantImageName;
    }
}
