package com.example.e_waste.model.profile;

public class DetailsResponse {

    private int user_id;
    private String address;
    private String longitude;
    private String latitude;
    private int auth_id;
    private String firstname;
    private String lastname;
    private String email;

    public DetailsResponse() {

    }

    public DetailsResponse(int user_id, String address, String longitude, String latitude, int auth_id, String firstname, String lastname, String email) {
        this.user_id = user_id;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.auth_id = auth_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(int auth_id) {
        this.auth_id = auth_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "DetailsResponse{" +
                "user_id=" + user_id +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", auth_id=" + auth_id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
