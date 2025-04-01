package org.example.yourstockv2backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "personal_details")
public class PersonalDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('personal_details_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Size(max = 255)
    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(max = 255)
    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 255)
    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy = "personalDetails")
    private Set<Customer> customers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "personalDetails")
    private Set<Employee> employees = new LinkedHashSet<>();

    @OneToMany(mappedBy = "personalDetails")
    private Set<Supplier> suppliers = new LinkedHashSet<>();
}