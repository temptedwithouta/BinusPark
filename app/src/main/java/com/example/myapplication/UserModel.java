package com.example.myapplication;

public class UserModel {
    String name, email, username, password;
    int phonenum;
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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getPhonenum() {
        return phonenum;
    }
    public void setPhonenum(int phonenum) {
        this.phonenum = phonenum;
    }
    public UserModel(String name, String email, String username, String password, int phonenum) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phonenum = phonenum;
    }
    public UserModel() {
    }
}
