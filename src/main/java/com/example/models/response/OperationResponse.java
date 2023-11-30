package com.example.models.response;

import com.example.models.enums.Transaction;

import java.time.LocalDate;

public record OperationResponse(
        Integer id,
        Transaction transaction,
        LocalDate dateTime,
        double amount,
        String ibanSource,
        String ibanDetination
) {}
