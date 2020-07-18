package com.example.androidjava.Model;

public class mDriver {
    private String id;
    private String userId;
    private String driverLinceneceno;
    private String driverNumberPlate;
    private String driverLincenecenoPhoto;
    private String driverNumberPlatePhoto;
    private String isAvailable;
    private String isAutoAccepted;
    private String latitude;
    private String longitude;
    private String vehicleType;
    private String isBulkDelivery;
    private String isOnDeliveryWork;
    public mDriver(String id, String userId, String driverLinceneceno, String driverNumberPlate, String driverLincenecenoPhoto, String driverNumberPlatePhoto, String isAvailable, String isAutoAccepted, String latitude, String longitude, String vehicleType) {
        this.id = id;
        this.userId = userId;
        this.driverLinceneceno = driverLinceneceno;
        this.driverNumberPlate = driverNumberPlate;
        this.driverLincenecenoPhoto = driverLincenecenoPhoto;
        this.driverNumberPlatePhoto = driverNumberPlatePhoto;
        this.isAvailable = isAvailable;
        this.isAutoAccepted = isAutoAccepted;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vehicleType = vehicleType;
    }

    public mDriver() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverLinceneceno() {
        return driverLinceneceno;
    }

    public void setDriverLinceneceno(String driverLinceneceno) {
        this.driverLinceneceno = driverLinceneceno;
    }

    public String getDriverNumberPlate() {
        return driverNumberPlate;
    }

    public void setDriverNumberPlate(String driverNumberPlate) {
        this.driverNumberPlate = driverNumberPlate;
    }

    public String getDriverLincenecenoPhoto() {
        return driverLincenecenoPhoto;
    }

    public void setDriverLincenecenoPhoto(String driverLincenecenoPhoto) {
        this.driverLincenecenoPhoto = driverLincenecenoPhoto;
    }

    public String getDriverNumberPlatePhoto() {
        return driverNumberPlatePhoto;
    }

    public void setDriverNumberPlatePhoto(String driverNumberPlatePhoto) {
        this.driverNumberPlatePhoto = driverNumberPlatePhoto;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getIsAutoAccepted() {
        return isAutoAccepted;
    }

    public void setIsAutoAccepted(String isAutoAccepted) {
        this.isAutoAccepted = isAutoAccepted;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getIsBulkDelivery() {
        return isBulkDelivery;
    }

    public void setIsBulkDelivery(String isBulkDelivery) {
        this.isBulkDelivery = isBulkDelivery;
    }

    public String getIsOnDeliveryWork() {
        return isOnDeliveryWork;
    }

    public void setIsOnDeliveryWork(String isOnDeliveryWork) {
        this.isOnDeliveryWork = isOnDeliveryWork;
    }
}
