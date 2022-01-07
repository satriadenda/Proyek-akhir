package com.udinus.newsahkesport.model;

public class UserAccount {

    private String fullname;
    private String phone;

    public String getFullname(){ return fullname;}
    public String getPhone() { return phone; }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }
}
