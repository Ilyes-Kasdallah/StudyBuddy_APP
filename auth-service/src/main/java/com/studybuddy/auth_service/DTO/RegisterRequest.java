package com.studybuddy.auth_service.DTO;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.BatchSize;

public class RegisterRequest {

    @NotNull
    private String username;

    // add constraints for password here
    private String password;

    private String role; // Default: "USER"

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
