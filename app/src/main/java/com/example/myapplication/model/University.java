package com.example.myapplication.model;

public class University {
    private final String name;

    private final String location;

    private final String latitude;

    private final String longitude;

    private final String parkingSlot;

    public University(String name, String location, String latitude, String longitude, String parkingSlot){
        this.name = name;

        this.location = location;

        this.latitude = latitude;

        this.longitude = longitude;

        this.parkingSlot = parkingSlot;
    }

    public String getName(){
        return this.name;
    }

    public String getLocation(){
        return this.location;
    }

    public String getLatitude(){
        return this.latitude;
    }

    public String getLongitude(){
        return this.longitude;
    }

    public String getParkingSlot(){
        return this.parkingSlot;
    }
}
