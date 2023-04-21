package com.example.e_waste.model;

import java.util.List;

public class TokenResponse {

    public List<Token> token;

    public TokenResponse(List<Token> token) {
        this.token = token;
    }

    public List<Token> getToken() {
        return token;
    }

    public void setToken(List<Token> token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "token=" + token +
                '}';
    }
}
