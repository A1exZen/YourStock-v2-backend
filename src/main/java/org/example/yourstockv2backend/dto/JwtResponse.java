package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String username;
    private String role;

    public JwtResponse(String accessToken, String username, String role) {
        this.accessToken = accessToken;
        this.username = username;
        this.role = role;
    }
}