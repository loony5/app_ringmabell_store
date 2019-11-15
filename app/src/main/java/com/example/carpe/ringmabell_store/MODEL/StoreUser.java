package com.example.carpe.ringmabell_store.MODEL;

public class StoreUser {

    private  String no, id, name, email, phone, photo;

    public StoreUser(String no, String id, String name, String email, String phone, String photo) {
        this.no = no;
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
    }

    public String getNo() {
        return no;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoto() {
        return photo;
    }
}
