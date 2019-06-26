package com.example.supraja_pc.idealmart.Model;

public class Users {
    public String phone, password,email,name;

    public Users()
    {

    }


    public Users(String phone, String password, String email,String name) {

        this.phone = phone;
        this.password = password;
        this.email = email;
        this.name=name;

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
