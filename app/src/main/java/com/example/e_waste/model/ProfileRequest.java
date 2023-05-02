package com.example.e_waste.model;

public class ProfileRequest {

    private String address;
    private Double longitude;
    private Double latitude;
    private int auth;

    public ProfileRequest() {

    }


    public ProfileRequest(String address, Double longitude, Double latitude, int auth) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.auth = auth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "ProfileRequest{" +
                "address='" + address + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", auth=" + auth +
                '}';
    }
}
