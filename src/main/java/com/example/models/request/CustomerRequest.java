package com.example.models.request;

import com.example.models.AccountEntity;
import com.example.models.enums.AccountType;
import com.example.models.CustomerEntity;
import com.example.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    private String dni;
    private String name;
    private String lastName;
    private LocalDate dob;
    private String email;
    private String mobile;
    private Role  role;
    private AccountType accountName;
    private double balance;

    public CustomerEntity toCustomerEntity(){
        return CustomerEntity.builder()
                .dni(this.getDni())
                .name(this.getName())
                .lastName(this.getLastName())
                .dob(this.getDob())
                .email(this.getEmail())
                .mobile(this.getMobile())
                .password("1234")
                .role(this.getRole())
                .accounts(Set.of(
                        new AccountEntity("IBAN-123",accountName,balance)
                ))
                .build();
    }
}
