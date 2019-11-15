package com.example.carpe.ringmabell_store.MODEL;


import java.util.List;

public class LoginResponse {

    private boolean success;
    private String message;
    private StoreUser storeUser;
    private List<StoreUser> detail;

    public LoginResponse(boolean success, String message, StoreUser storeUser, List<StoreUser> detail) {
        this.success = success;
        this.message = message;
        this.storeUser = storeUser;
        this.detail = detail;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public StoreUser getStoreUser() {
        return storeUser;
    }

    public List<StoreUser> getDetail() {
        return detail;
    }
}
