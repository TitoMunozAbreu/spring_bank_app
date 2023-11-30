package com.example.models;

import com.example.models.enums.AccountType;
import com.example.models.enums.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AccountEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;
    private String iban;
    @Enumerated(EnumType.STRING)
    private AccountType name;
    private double balance;

    @ManyToMany
    @JoinTable(
            name = "customer_account",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private List<CustomerEntity> customers;

    @OneToMany(mappedBy = "account")
    private List<OperationEntity> operations;

    public AccountEntity(String iban, AccountType name, double balance) {
        this.name = name;
        this.balance = balance;
        this.iban = iban;
    }

    public AccountEntity(String iban, AccountType name, double balance, List<CustomerEntity> customers) {
        this.name = name;
        this.balance = balance;
        this.customers = customers;
        this.iban = iban;
    }

    public static String generateIban(){
        String ibanBase = "ES21";
        int min = 0;
        int max = 9;
        int range = max - min + 1;

        for (int i = 0; i < 20; i++) {
            int random = (int) ((Math.random()* range) + min);
            ibanBase += String.valueOf(random);
        }
        return ibanBase;
    }

    public void transaction(Transaction transaction, double amount){
        switch (transaction){
            case DEPOSIT -> deposit(amount);
            case TRANSFER, WITHDRAW -> withdrawAndTransfer(amount);
        }

    }

    private void deposit(double amount){
        this.balance += amount;
    }

    private void withdrawAndTransfer(double amount){
        this.balance -= amount;
    }
}
