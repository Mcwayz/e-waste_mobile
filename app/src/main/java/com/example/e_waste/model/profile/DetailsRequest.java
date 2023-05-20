package com.example.e_waste.model.profile;

public class DetailsRequest {


    private int user_id;

    public DetailsRequest() {

    }

    public DetailsRequest(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "DetailsRequest{" +
                "user_id=" + user_id +
                '}';
    }

}
