package com.example.models.request;

import com.example.models.AccountEntity;
import com.example.models.CustomerEntity;
import com.example.models.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    private AccountType accountName;
    private double balance;

    public AccountEntity toAccountEntity(CustomerEntity customer){
        return AccountEntity.builder()
                .iban("IBAN-456")
                .name(this.getAccountName())
                .balance(this.getBalance())
                .customers(List.of(customer))
                .build();
    }
}
