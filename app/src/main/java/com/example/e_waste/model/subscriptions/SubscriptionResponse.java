package com.example.e_waste.model.subscriptions;

public class SubscriptionResponse {

    private int user;
    private int waste;
    private int sub_id;
    private String sub_date;


    public SubscriptionResponse() {
    }

    public SubscriptionResponse(int user, int waste, int sub_id, String sub_date) {
        this.user = user;
        this.waste = waste;
        this.sub_id = sub_id;
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

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_date() {
        return sub_date;
    }

    public void setSub_date(String sub_date) {
        this.sub_date = sub_date;
    }

    @Override
    public String toString() {
        return "SubscriptionResponse{" +
                "user=" + user +
                ", waste=" + waste +
                ", sub_id=" + sub_id +
                ", sub_date='" + sub_date + '\'' +
                '}';
    }
}
