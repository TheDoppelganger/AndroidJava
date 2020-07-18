package com.example.androidjava.Model;

public class mUser {
    private String id;
    private String user_type;
    private String adharcardNo;
    private String name;
    private String address;
    private String pincode;
    private String mobile_no;
    private String email;
    private String password;
    private String isSwitch;
    private String latitude;
    private String longitude;
    public mUser(String user_type, String adharcardNo, String name, String address, String pincode, String mobile_no, String email, String password) {
        this.user_type = user_type;
        this.adharcardNo = adharcardNo;
        this.name = name;
        this.address = address;
        this.pincode = pincode;
        this.mobile_no = mobile_no;
        this.email = email;
        this.password = password;
    }

    public mUser(String id, String user_type, String adharcardNo, String name, String address, String pincode, String mobile_no, String email, String password) {
        this.id = id;
        this.user_type = user_type;
        this.adharcardNo = adharcardNo;
        this.name = name;
        this.address = address;
        this.pincode = pincode;
        this.mobile_no = mobile_no;
        this.email = email;
        this.password = password;
    }

    public mUser() {
    }


    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getAdharcardNo() {
        return adharcardNo;
    }

    public void setAdharcardNo(String adharcardNo) {
        this.adharcardNo = adharcardNo;
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

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getIsSwitch() {
        return isSwitch;
    }

    public void setIsSwitch(String isSwitch) {
        this.isSwitch = isSwitch;
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
}
