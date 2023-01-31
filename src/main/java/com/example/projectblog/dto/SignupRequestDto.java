package com.example.projectblog.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;

    private boolean admin = false;

    private String adminToken = "";

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getAdminToken() {
        return adminToken;
    }
}