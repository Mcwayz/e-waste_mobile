package com.example.e_waste.model.profile;



public class UserResponse {

    private int user_id;
    private String status;

    public UserResponse() {
    }

    public UserResponse(int user_id, String status) {
        this.user_id = user_id;
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "user_id=" + user_id +
                ", status=" + status +
                '}';
    }
}
