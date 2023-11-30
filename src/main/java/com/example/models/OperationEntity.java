package com.example.models;

import com.example.models.enums.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OperationEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Transaction transaction;
    @Column(columnDefinition = "date")
    private LocalDate dateTime;
    private double amount;
    private String ibanSource;
    private String ibanDestination;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;
}
