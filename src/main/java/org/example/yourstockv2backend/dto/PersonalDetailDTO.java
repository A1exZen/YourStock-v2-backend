package org.example.yourstockv2backend.dto;

import lombok.Data;

@Data
public class PersonalDetailDTO {
    private Long id;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
}
