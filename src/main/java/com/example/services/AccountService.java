package com.example.services;

import com.example.models.AccountEntity;
import com.example.models.CustomerEntity;
import com.example.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public void create(CustomerEntity customer) {
        //listar las cuentas del cliente
        List<AccountEntity> accountsEntities = customer.getAccounts().stream()
                .map(account -> new AccountEntity(
                        account.getIban(),
                        account.getName(),
                        account.getBalance(),
                        List.of(customer)))
                .collect(Collectors.toList());

        accountRepository.saveAll(accountsEntities);
    }


    public void deleteAccountsByCustomer(CustomerEntity customerFound) {
        customerFound.getAccounts().stream()
                .forEach(account -> accountRepository.deleteById(account.getId()));


    }
}
