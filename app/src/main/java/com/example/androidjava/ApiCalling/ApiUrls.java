package com.example.androidjava.ApiCalling;

public class ApiUrls {
    public static final String rootUrl = "http://trippleatt.com/Application/";
    public static final String registration = rootUrl + "application_registration.php";
    public static final String login = rootUrl + "application_login.php";
    public static final String addProduct = rootUrl + "application_addproduct.php";
    public static final String fetchProduct = rootUrl + "application_listproduct.php";
    public static final String orderOfflineBilling = rootUrl + "application_seller_billing.php";
    public static final String getShopAllNearBy = rootUrl + "application_fetchShops.php";
    public static final String tempCartOperation = rootUrl + "application_tempCart.php";
    public static final String orderCustomerOnline = rootUrl + "application_customer_order_online.php";
    public static final String orderHistoryCustomerOnline = rootUrl + "application_customer_fetch_order_online.php";
    public static final String orderHistorySellerOnline = rootUrl + "application_seller_shopOrders.php";
    public static final String getDriverOrder = rootUrl + "application_fetch_order_driver.php";
    public static final String updateDriverOrderStatus = rootUrl + "application_allocate_driver.php";
    public static final String updateDriverLocation = rootUrl + "application_updateLocation.php";
    public static final String submitReview = rootUrl + "application_update_review.php";
    public static final String suggestEarn = rootUrl + "application_customer_suggest_earn.php";
    public static final String issues = rootUrl + "application_issues_operation.php";
    public static final String driverSimpleRequest = rootUrl + "application_driver_simple_request.php";
    public static final String driverCompletedOrder = rootUrl + "application_completed_order_driver.php";
}
