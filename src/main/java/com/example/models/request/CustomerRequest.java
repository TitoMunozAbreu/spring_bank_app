package com.example.models.request;

import com.example.models.AccountEntity;
import com.example.models.enums.AccountType;
import com.example.models.CustomerEntity;
import com.example.models.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,9}[A-Za-z]$",
            message = "ID validos, ejemplos: DNI 12345678A | NIE X12345678A")
    private String dni;

    @NotBlank(message = "Must include an email")
    private String name;

    @NotBlank(message = "Must include an email")
    private String lastName;

    @NotNull
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @Past(message = "date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Must include an email")
    @Email
    @Pattern(regexp = ".*@mail\\.com", message = "Must include a valid domain = example@mail.com")
    private String email;

    @NotBlank(message = "Must include a password")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&()-+]).{8}$",
            message = "At least 8 characters in length, Include at least one uppercase letter. Include at least one symbol (!@#$%^&*()_+). ")
    private String password;

    @NotBlank(message = "Must include a mobile")
    @Length(min = 9, max = 9, message = "Mobile 9 digits length")
    private String mobile;

    private Role  role;
    private AccountType accountName;

    @PositiveOrZero(message = "Balance must be a positive number")
    private double balance;
    private String iban;

    public CustomerEntity toCustomerEntity(){
        //iban generator
        String generatedIban = AccountEntity.generateIban();
        this. iban= generatedIban;
        return CustomerEntity.builder()
                .dni(this.getDni())
                .name(this.getName())
                .lastName(this.getLastName())
                .dob(this.getDob())
                .email(this.getEmail())
                .mobile(this.getMobile())
                .password(this.getPassword())
                .role(this.getRole())
                .accounts(Set.of(
                        new AccountEntity(generatedIban,accountName,balance)
                ))
                .build();
    }
}
