package com.example.carpe.ringmabell_store.MODEL;

public class StoreRegisterResponse {

    private String success, message;

    public StoreRegisterResponse(String success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
