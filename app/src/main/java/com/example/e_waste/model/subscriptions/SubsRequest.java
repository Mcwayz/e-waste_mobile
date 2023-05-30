package com.example.e_waste.model.subscriptions;

public class SubsRequest {
    private int user_id;

    public SubsRequest() {
    }

    public SubsRequest(int user_id) {
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
        return "SubsRequest{" +
                "user_id=" + user_id +
                '}';
    }
}
