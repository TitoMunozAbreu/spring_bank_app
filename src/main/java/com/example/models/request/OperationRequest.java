package com.example.models.request;

import com.example.models.AccountEntity;
import com.example.models.OperationEntity;
import com.example.models.enums.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationRequest {
    private Transaction transaction;
    private LocalDateTime dateTime;
    private double amount;
    private String ibanSource;
    private String ibaDetination;

    public OperationEntity toOperationEntity(AccountEntity account){
        return OperationEntity.builder()
                .transaction(getTransaction())
                .dateTime(getDateTime())
                .amount(getAmount())
                .ibanSource(getIbanSource())
                .ibanDestination(getIbaDetination())
                .account(account)
                .build();
    }

}
