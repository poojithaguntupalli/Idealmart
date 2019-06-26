package com.example.supraja_pc.idealmart.Model;

public class Address {

    public String city,house,pincode,state,street;

    public Address()
    {

    }

    public Address(String city, String house, String pincode, String state, String street) {
        this.city = city;
        this.house = house;
        this.pincode = pincode;
        this.state = state;
        this.street = street;
    }
}
