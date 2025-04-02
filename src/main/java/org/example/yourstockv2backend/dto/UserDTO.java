package org.example.yourstockv2backend.dto;

import lombok.Data;
import org.example.yourstockv2backend.model.enums.Role;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private Role role;
    private EmployeeDTO employee;
}

