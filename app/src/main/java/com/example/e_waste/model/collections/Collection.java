package com.example.e_waste.model.collections;

public class Collection {

    private int task_id;
    private int collection_id;
    private int collector_id;
    private int user;
    private String address;
    private String longitude;
    private String latitude;
    private boolean is_collected;
    private String request_date;
    private String assigned_date;
    private String user_collect_date;
    private String date_closed;

    public Collection() {
    }

    public Collection(int task_id, int collection_id, int collector_id, int user, String address, String longitude, String latitude, boolean is_collected, String request_date, String assigned_date, String user_collect_date, String date_closed) {
        this.task_id = task_id;
        this.collection_id = collection_id;
        this.collector_id = collector_id;
        this.user = user;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.is_collected = is_collected;
        this.request_date = request_date;
        this.assigned_date = assigned_date;
        this.user_collect_date = user_collect_date;
        this.date_closed = date_closed;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(int collection_id) {
        this.collection_id = collection_id;
    }

    public int getCollector_id() {
        return collector_id;
    }

    public void setCollector_id(int collector_id) {
        this.collector_id = collector_id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
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

    public boolean isIs_collected() {
        return is_collected;
    }

    public void setIs_collected(boolean is_collected) {
        this.is_collected = is_collected;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public String getAssigned_date() {
        return assigned_date;
    }

    public void setAssigned_date(String assigned_date) {
        this.assigned_date = assigned_date;
    }

    public String getUser_collect_date() {
        return user_collect_date;
    }

    public void setUser_collect_date(String user_collect_date) {
        this.user_collect_date = user_collect_date;
    }

    public String getDate_closed() {
        return date_closed;
    }

    public void setDate_closed(String date_closed) {
        this.date_closed = date_closed;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "task_id=" + task_id +
                ", collection_id=" + collection_id +
                ", collector_id=" + collector_id +
                ", user=" + user +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", is_collected=" + is_collected +
                ", request_date='" + request_date + '\'' +
                ", assigned_date='" + assigned_date + '\'' +
                ", user_collect_date='" + user_collect_date + '\'' +
                ", date_closed='" + date_closed + '\'' +
                '}';
    }
}
