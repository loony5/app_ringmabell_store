package com.example.carpe.ringmabell_store.MODEL;

public class StoreInfo {

    private String storeNo, storeName, startTime, finishTime, personalDay, address, introduce;

    public StoreInfo(String storeNo, String storeName, String startTime, String finishTime, String personalDay, String address, String introduce) {
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.personalDay = personalDay;
        this.address = address;
        this.introduce = introduce;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public String getPersonalDay() {
        return personalDay;
    }

    public String getAddress() {
        return address;
    }

    public String getIntroduce() {
        return introduce;
    }
}
