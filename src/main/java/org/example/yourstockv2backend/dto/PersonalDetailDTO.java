package org.example.yourstockv2backend.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PersonalDetailDTO {
    private Long id;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private OffsetDateTime createdAt;
}