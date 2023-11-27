package com.example.models;

import com.example.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "UniqueDniAndEmail",
                columnNames = {"dni", "email"})
})
public class CustomerEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dni;
    private String name;
    private String lastName;
    @Column(columnDefinition = "date")
    private LocalDate dob;
    private String email;
    private String password;
    private String mobile;
    @ManyToMany(mappedBy = "customers")
    private Set<AccountEntity> accounts;
    @Enumerated(EnumType.STRING)
    private Role role;



}
