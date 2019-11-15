package com.example.carpe.ringmabell_store.MODEL;

import java.util.List;

public class StoreInfoReadResponse {

    private String success;
    private List<StoreInfo> read;

    public StoreInfoReadResponse(String success, List<StoreInfo> read) {
        this.success = success;
        this.read = read;
    }

    public String getSuccess() {
        return success;
    }

    public List<StoreInfo> getRead() {
        return read;
    }
}
