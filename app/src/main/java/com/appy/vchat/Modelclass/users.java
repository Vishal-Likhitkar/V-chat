package com.appy.vchat.Modelclass;

public class users {
    String uid;
    public String name;
    String email;
    public String imageuri;
    public String status;

    public users() {
    }

    public users(String uid, String name, String email, String imageuri, String status) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageuri = imageuri;
        this.status = status;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
