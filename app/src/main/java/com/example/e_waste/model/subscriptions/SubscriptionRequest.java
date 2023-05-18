package com.example.e_waste.model.subscriptions;

public class SubscriptionRequest {

    private int user;
    private int waste;
    private String sub_date;

    public SubscriptionRequest() {

    }

    public SubscriptionRequest(int user, int waste, String sub_date) {
        this.user = user;
        this.waste = waste;
        this.sub_date = sub_date;
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

    public String getSub_date() {
        return sub_date;
    }

    public void setSub_date(String sub_date) {
        this.sub_date = sub_date;
    }

    @Override
    public String toString() {
        return "SubscriptionRequest{" +
                "user=" + user +
                ", waste=" + waste +
                ", sub_date='" + sub_date + '\'' +
                '}';
    }
}
