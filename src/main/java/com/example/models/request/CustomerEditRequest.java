package com.example.models.request;

import com.example.models.AccountEntity;
import com.example.models.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEditRequest {
    private String dni;
    private String name;
    private String lastName;
    private LocalDate dob;
    private String email;
    private String mobile;

    public CustomerEntity toCustomerEntity(){
        return CustomerEntity.builder()
                .dni(this.getDni())
                .name(this.getName())
                .lastName(this.getLastName())
                .dob(this.getDob())
                .email(this.getEmail())
                .mobile(this.getMobile())
                .password("1234")
                .build();
    }
}
