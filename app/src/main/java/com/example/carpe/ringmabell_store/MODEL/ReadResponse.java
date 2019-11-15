package com.example.carpe.ringmabell_store.MODEL;

import java.util.List;

public class ReadResponse {

    private String success;
    private List<StoreUser> read;

    public ReadResponse(String success, List<StoreUser> read) {
        this.success = success;
        this.read = read;
    }

    public String getSuccess() {
        return success;
    }

    public List<StoreUser> getRead() {
        return read;
    }
}
