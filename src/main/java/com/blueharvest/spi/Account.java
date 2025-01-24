package com.blueharvest.spi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    private BigDecimal balance;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<Transaction> transactions;

    public Account(String name) {
        this.name = name;
        this.balance = BigDecimal.ZERO;
    }

    public BigDecimal addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        BigDecimal value = transaction.getAmount();
        balance = value.compareTo(BigDecimal.ZERO) < 0 ? balance.subtract(value) : balance.add(value);
        transactions.add(transaction);
        return balance;
    }
}
