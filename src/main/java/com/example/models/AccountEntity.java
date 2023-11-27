package com.example.models;

import com.example.models.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
        this.iban = iban;
        this.name = name;
        this.balance = balance;
    }

    public AccountEntity(String iban, AccountType name, double balance, List<CustomerEntity> customers) {
        this.iban = iban;
        this.name = name;
        this.balance = balance;
        this.customers = customers;
    }
}
