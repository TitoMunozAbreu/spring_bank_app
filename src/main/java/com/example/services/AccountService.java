package com.example.services;

import com.example.models.AccountEntity;
import com.example.models.CustomerEntity;
import com.example.models.OperationEntity;
import com.example.models.enums.AccountType;
import com.example.repositories.AccountRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void create(AccountType name, double balance, CustomerEntity customer) {
/*        //listar las cuentas del cliente
        List<AccountEntity> accountsEntities = customer.getAccounts().stream()
                .map(account -> new AccountEntity(
                        account.getIban(),
                        account.getName(),
                        account.getBalance(),
                        List.of(customer)))
                .collect(Collectors.toList());

        accountRepository.saveAll(accountsEntities);*/

        AccountEntity account = AccountEntity.builder()
                .name(name)
                .balance(balance)
                .customers(List.of(customer))
                .build();

        accountRepository.save(account);
    }

    @Transactional
    public void deleteAccountsByCustomer(CustomerEntity customerFound) {
        customerFound.getAccounts().stream()
                .forEach(account -> accountRepository.deleteById(account.getId()));


    }

    @Transactional
    public AccountEntity findById(Integer id) {
        return accountRepository.findById(id).get();
    }

    @Transactional
    public void addOperation(Integer id, OperationEntity operation) {
        //buscar la account segun ID
        AccountEntity account = accountRepository.findById(id).get();
        //añadir la nueva operacion
        account.getOperations().add(operation);
        //guardar la account con los cambios
        accountRepository.save(account);
    }
}
