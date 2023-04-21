package com.example.e_waste.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class User implements Parcelable {
    private int user_id;
    private String username;
    private String firstname;
    private String lastname;
    private String address;
    private String longitude;
    private String latitude;

    public User() {
    }

    public User(int user_id, String username, String firstname, String lastname, String address, String longitude, String latitude) {
        this.user_id = user_id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    protected User(Parcel in) {
        user_id = in.readInt();
        username = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        address = in.readString();
        longitude = in.readString();
        latitude = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(user_id);
        dest.writeString(username);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(address);
        dest.writeString(longitude);
        dest.writeString(latitude);
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
