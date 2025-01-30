package com.greengrass.spi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity
@Setter
@Getter
@SuppressWarnings("javaarchitecture:S7027") // Account has Customer
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private BigDecimal balance;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private List<Transaction> transactions;

    public Account() {
        this.balance = BigDecimal.ZERO;
    }

    public Account(BigDecimal balance) {
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        this.balance = balance;
    }

    public BigDecimal addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
        return balance;
    }
}
