package com.studybuddy.auth_service.DTO;

public class TokenValidationResponse {
    private boolean valid;
    private Integer userId;

    public TokenValidationResponse(boolean valid, Integer userId) {
        this.valid = valid;
        this.userId = userId;
    }

    // Getters
    public boolean isValid() {
        return valid;
    }

    public Integer getUserId() {
        return userId;
    }
}
