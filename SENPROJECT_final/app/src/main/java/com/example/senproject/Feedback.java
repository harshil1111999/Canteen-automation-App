package com.example.senproject;

public class Feedback {
    private String CanteenId;
    private String Message;

    public Feedback(){

    }

    public Feedback(String canteenId, String message) {
        CanteenId = canteenId;
        Message = message;
    }

    public String getCanteenId() {
        return CanteenId;
    }

    public void setCanteenId(String canteenId) {
        CanteenId = canteenId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
