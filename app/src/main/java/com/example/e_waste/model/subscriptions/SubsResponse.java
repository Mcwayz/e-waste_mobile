package com.example.e_waste.model.subscriptions;


import java.util.List;

public class SubsResponse {
    private List<Subscription> subscription;

    public SubsResponse(List<Subscription> subscription) {
        this.subscription = subscription;
    }

    public List<Subscription> getSubscription() {
        return subscription;
    }

    public void setSubscription(List<Subscription> subscription) {
        this.subscription = subscription;
    }
}