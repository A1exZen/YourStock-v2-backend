package org.example.yourstockv2backend.dto;

import lombok.Data;
import org.example.yourstockv2backend.model.enums.Role;

@Data
public class JwtResponse {
    private String accessToken;
    private String type;
    private String username;
    private Role role;
    private Long userId;

    public JwtResponse(String accessToken, String type, String username, Role role, Long userId) {
        this.accessToken = accessToken;
        this.type = type;
        this.username = username;
        this.role = role;
        this.userId = userId;
    }

    public JwtResponse(String accessToken, String username, String role) {
        this.accessToken = accessToken;
        this.type = "Bearer";
        this.username = username;
        this.role = Role.valueOf(role);
        this.userId = null;
    }
}