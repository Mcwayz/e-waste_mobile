package com.example.e_waste.model.subscriptions;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Subscription implements Parcelable {

    private int sub_id;
    private String firstname;
    private String lastname;
    private String address;
    private String waste_type;
    private String monthly_price;
    private String sub_date;
    private int auth_id;


    public Subscription() {
    }

    public Subscription(int sub_id, String firstname, String lastname, String address, String waste_type, String monthly_price, String sub_date, int auth_id) {
        this.sub_id = sub_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.waste_type = waste_type;
        this.monthly_price = monthly_price;
        this.sub_date = sub_date;
        this.auth_id = auth_id;
    }

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
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

    public String getWaste_type() {
        return waste_type;
    }

    public void setWaste_type(String waste_type) {
        this.waste_type = waste_type;
    }

    public String getMonthly_price() {
        return monthly_price;
    }

    public void setMonthly_price(String monthly_price) {
        this.monthly_price = monthly_price;
    }

    public String getSub_date() {
        return sub_date;
    }

    public void setSub_date(String sub_date) {
        this.sub_date = sub_date;
    }

    public int getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(int auth_id) {
        this.auth_id = auth_id;
    }

    protected Subscription(Parcel in) {
        sub_id = in.readInt();
        firstname = in.readString();
        lastname = in.readString();
        address = in.readString();
        waste_type = in.readString();
        monthly_price = in.readString();
        sub_date = in.readString();
        auth_id = in.readInt();
    }

    public static final Creator<Subscription> CREATOR = new Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel in) {
            return new Subscription(in);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(sub_id);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(address);
        dest.writeString(waste_type);
        dest.writeString(monthly_price);
        dest.writeString(sub_date);
        dest.writeInt(auth_id);
    }
}
