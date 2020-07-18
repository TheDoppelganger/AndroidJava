package com.example.androidjava.Model;

public class mRating {
    private String id;
    private String name;
    private String rating;
    private String ratingComments;
    private String shopOrDriverOrProductId;
    private String shopOrDriverOrProduct;
    public mRating() {
    }

    public mRating(String id, String name, String rating, String ratingComments) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.ratingComments = ratingComments;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingComments() {
        return ratingComments;
    }

    public void setRatingComments(String ratingComments) {
        this.ratingComments = ratingComments;
    }

    public String getShopOrDriverOrProductId() {
        return shopOrDriverOrProductId;
    }

    public void setShopOrDriverOrProductId(String shopOrDriverOrProductId) {
        this.shopOrDriverOrProductId = shopOrDriverOrProductId;
    }

    public String getShopOrDriverOrProduct() {
        return shopOrDriverOrProduct;
    }

    public void setShopOrDriverOrProduct(String shopOrDriverOrProduct) {
        this.shopOrDriverOrProduct = shopOrDriverOrProduct;
    }
}
