package com.example.e_waste.model.subscriptions;

public class SubscriptionRequest {

    private int user;
    private int waste;

    public SubscriptionRequest() {

    }

    public SubscriptionRequest(int user, int waste) {
        this.user = user;
        this.waste = waste;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getWaste() {
        return waste;
    }

    public void setWaste(int waste) {
        this.waste = waste;
    }

    @Override
    public String toString() {
        return "SubscriptionRequest{" +
                "user=" + user +
                ", waste=" + waste +
                '}';
    }
}
