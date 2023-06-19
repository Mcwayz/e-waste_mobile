package com.example.e_waste.model.collections;

public class CollectRequest {

    private int user_id;

    public CollectRequest() {
    }

    public CollectRequest(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "CollectRequest{" +
                "user_id=" + user_id +
                '}';
    }
}
