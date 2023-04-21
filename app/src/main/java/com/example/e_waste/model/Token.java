package com.example.e_waste.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Token implements Parcelable {

    private String refresh;
    private String token;



    public Token(String refresh, String token) {
        this.refresh = refresh;
        this.token = token;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    protected Token(Parcel in) {
        refresh = in.readString();
        token = in.readString();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(refresh);
        dest.writeString(token);
    }


    @Override
    public String toString() {
        return "Token{" +
                "refresh='" + refresh + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
