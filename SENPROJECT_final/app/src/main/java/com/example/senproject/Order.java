package com.example.senproject;

public class Order {
    private String OrderNo;
    private String CustomerId;
    private String CanteenId;
    private String CanteenName;
    private String OrderDetails;
    private String OrderDateTime;
    private String CookingInstruction;
    private String PaymetMethod;

    public Order(){

    }

    public Order(String orderNo, String customerId, String canteenId, String canteenName, String orderDetails, String orderDateTime, String cookingInstruction, String paymetMethod) {
        OrderNo = orderNo;
        CustomerId = customerId;
        CanteenId = canteenId;
        CanteenName = canteenName;
        OrderDetails = orderDetails;
        OrderDateTime = orderDateTime;
        CookingInstruction = cookingInstruction;
        PaymetMethod = paymetMethod;
    }

    public String getPaymetMethod() {
        return PaymetMethod;
    }

    public void setPaymetMethod(String paymetMethod) {
        PaymetMethod = paymetMethod;
    }

    public String getCookingInstruction() {
        return CookingInstruction;
    }

    public void setCookingInstruction(String cookingInstruction) {
        CookingInstruction = cookingInstruction;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCanteenId() {
        return CanteenId;
    }

    public void setCanteenId(String canteenId) {
        CanteenId = canteenId;
    }

    public String getCanteenName() {
        return CanteenName;
    }

    public void setCanteenName(String canteenName) {
        CanteenName = canteenName;
    }

    public String getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        OrderDetails = orderDetails;
    }

    public String getOrderDateTime() {
        return OrderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        OrderDateTime = orderDateTime;
    }
}
