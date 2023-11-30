package com.example.services;

import com.example.models.AccountEntity;
import com.example.models.CustomerEntity;
import com.example.models.OperationEntity;
import com.example.models.enums.AccountType;
import com.example.models.enums.Transaction;
import com.example.models.request.OperationRequest;
import com.example.models.response.AccountResponse;
import com.example.models.response.AccountsResponse;
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
    public void create(AccountEntity account) {

        accountRepository.save(account);
    }
    @Transactional
    public void create(CustomerEntity customer) {
        customer.getAccounts().stream()
                .forEach(account -> {
                    AccountEntity newAccount = AccountEntity.builder()
                            .iban(account.getIban())
                            .name(account.getName())
                            .balance(account.getBalance())
                            .customers(List.of(customer))
                            .build();
                    accountRepository.save(newAccount);
                });

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

    public AccountEntity findByIban(String iban) {
        return accountRepository.findByIban(iban);
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

    @Transactional
    public List<AccountsResponse> findAllNotIncludeCustomerInSession(List<AccountResponse> accountsInSession) {
        return toAccountsResponse(accountRepository.findAll(),accountsInSession);
    }

    private List<AccountsResponse> toAccountsResponse(List<AccountEntity> accountEntities, List<AccountResponse> accountsInSession) {
        //ID de accountInSesion
        List<Integer> accountInSessionIDs = accountsInSession.stream()
                .map(AccountResponse::id)
                .collect(Collectors.toList());

        List<AccountEntity> filteredAccounts = accountEntities.stream()
                .filter(account -> !accountInSessionIDs.contains(account.getId()))
                .collect(Collectors.toList());

        return filteredAccounts.stream()
                .map(account -> new AccountsResponse(account.getIban()))
                .collect(Collectors.toList());
    }


    public void registerOperation(OperationRequest request) {
        //alamcenar cuenta segun Iban source
        AccountEntity accountSource = findByIban(request.getIbanSource());
        // registrar transaction
        accountSource.transaction(request.getTransaction(), request.getAmount());

        if(request.getTransaction().equals(Transaction.TRANSFER)){
            //almacenar cuenta segun Iban destination
            AccountEntity accountDestination = findByIban(request.getIbanDestination());
            //registrar transaction
            accountDestination.transaction(Transaction.DEPOSIT, request.getAmount());
            //
            accountRepository.save(accountDestination);
        }

        //guardar account Source con los cambios
        accountRepository.save(accountSource);

    }
}
