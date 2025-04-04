
package org.example.yourstockv2backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.yourstockv2backend.model.enums.Status;

@Data
public class SupplierDTO {
    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 255, message = "Contact person must not exceed 255 characters")
    private String contactPerson;

    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Phone number must be a valid number")
    private String phone;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    private Status status;
}