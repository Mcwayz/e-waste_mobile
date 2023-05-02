package com.example.e_waste.model;

public class ProfileResponse {

    private String success;
    private int auth;

    public ProfileResponse() {

    }

    public ProfileResponse(String success, int auth) {
        this.success = success;
        this.auth = auth;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "ProfileResponse{" +
                "success='" + success + '\'' +
                ", auth=" + auth +
                '}';
    }
}
