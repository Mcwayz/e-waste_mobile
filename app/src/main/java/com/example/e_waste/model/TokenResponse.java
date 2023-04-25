package com.example.e_waste.model;

import java.util.List;

public class TokenResponse {
    String refresh;
    String access;

    public TokenResponse() {
    }

    public TokenResponse(String refresh, String access) {
        this.refresh = refresh;
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "refresh='" + refresh + '\'' +
                ", access='" + access + '\'' +
                '}';
    }
}
