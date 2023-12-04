package com.example.models.request;

import com.example.models.AccountEntity;
import com.example.models.CustomerEntity;
import com.example.models.enums.AccountType;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    private AccountType accountName;

    @PositiveOrZero(message = "Balance must be a positive number")
    private double balance;

    public AccountEntity toAccountEntity(CustomerEntity customer){

        return AccountEntity.builder()
                .iban(AccountEntity.generateIban())
                .name(this.getAccountName())
                .balance(this.getBalance())
                .customers(List.of(customer))
                .build();
    }
}
