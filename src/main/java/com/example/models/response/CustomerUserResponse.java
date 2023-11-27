package com.example.models.response;

import com.example.models.CustomerEntity;
import com.example.models.enums.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record CustomerUserResponse(
        Integer id,
        String name,
        String lastName,
        LocalDate dob,
        String email,
        Role role,
        String mobile,
        List<AccountResponse> accounts
) {
    public static CustomerUserResponse toCustomerUserResponse(CustomerEntity customerFound) {
        //accounts To AccountResponse
        List<AccountResponse> accountsResponse = customerFound.getAccounts().stream()
                .map(account -> new AccountResponse(
                        account.getIban(),
                        account.getName(),
                        account.getBalance(),
                        account.getOperations()
                                .stream()
                                //Operations to OperationResponse
                                .map(operation -> new OperationResponse(
                                        operation.getId(),
                                        operation.getTransaction(),
                                        operation.getDateTime(),
                                        operation.getAmount(),
                                        operation.getIbanSource(),
                                        operation.getIbanDestination()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        //Customer to CustomerUserResponse
        CustomerUserResponse customer = new CustomerUserResponse(
                customerFound.getId(),
                customerFound.getName(),
                customerFound.getLastName(),
                customerFound.getDob(),
                customerFound.getEmail(),
                customerFound.getRole(),
                customerFound.getMobile(),
                accountsResponse);

        return customer;
    }
}
