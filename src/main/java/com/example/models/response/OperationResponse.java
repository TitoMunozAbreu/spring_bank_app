package com.example.models.response;

import com.example.models.enums.Transaction;

import java.time.LocalDateTime;

public record OperationResponse(
        Integer id,
        Transaction transaction,
        LocalDateTime dateTime,
        double amount,
        String ibanSource,
        String ibaDetination
) {}
