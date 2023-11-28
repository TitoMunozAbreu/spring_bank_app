package com.example.models.response;

import com.example.models.enums.AccountType;

import java.util.List;

public record AccountResponse(
        Integer id,
        String iban,
        AccountType name,
        double balance,
        List<OperationResponse> operations
) {}
