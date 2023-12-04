package com.example.models.request;

import com.example.models.AccountEntity;
import com.example.models.OperationEntity;
import com.example.models.enums.Transaction;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationRequest {
    private Transaction transaction;

    @NotNull
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @FutureOrPresent(message = "date of transaction must be in the present or future")
    private LocalDate dateTime;

    @PositiveOrZero(message = "Balance must be a positive number")
    private double amount;

    private String ibanSource;
    private String ibanDestination;

    public OperationEntity toOperationEntity(AccountEntity account){
        return OperationEntity.builder()
                .transaction(getTransaction())
                .dateTime(getDateTime())
                .amount(getAmount())
                .ibanSource(getIbanSource())
                .ibanDestination(getIbanDestination())
                .account(account)
                .build();
    }

}
