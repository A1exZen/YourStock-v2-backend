package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String username;
    private String role;
    private Long userId;

    public JwtResponse(String accessToken, String username, String role, Long userId) {
        this.accessToken = accessToken;
        this.username = username;
        this.role = role;
        this.userId = userId;
    }
}