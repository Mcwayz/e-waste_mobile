package com.example.e_waste.model.profile;

public class ProfileRequest {

    private String address;
    private Double longitude;
    private Double latitude;
    private int auth_id;

    public ProfileRequest() {

    }

    public ProfileRequest(String address, Double longitude, Double latitude, int auth_id) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.auth_id = auth_id;
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

    public int getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(int auth_id) {
        this.auth_id = auth_id;
    }

    @Override
    public String toString() {
        return "ProfileRequest{" +
                "address='" + address + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", auth_id=" + auth_id +
                '}';
    }
}
