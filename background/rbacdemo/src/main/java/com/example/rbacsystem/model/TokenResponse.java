package com.example.rbacsystem.model;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
