package org.example.yourstockv2backend.dto;

import lombok.Data;
import org.example.yourstockv2backend.model.enums.Status;

import java.time.OffsetDateTime;

@Data
public class SupplierDTO {
    private Long id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private Status status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}