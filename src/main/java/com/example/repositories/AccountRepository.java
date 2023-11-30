package com.example.repositories;

import com.example.models.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity,Integer> {
    AccountEntity findByIban(String iban);
}
