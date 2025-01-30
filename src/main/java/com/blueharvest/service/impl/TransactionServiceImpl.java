package com.blueharvest.service.impl;

import com.blueharvest.domain.TransactionType;
import com.blueharvest.service.TransactionService;
import com.blueharvest.spi.Transaction;
import com.blueharvest.spi.repository.TransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactions;

    public TransactionServiceImpl(TransactionRepository transactions) {
        this.transactions = transactions;
    }

    @Override
    public Transaction deposit(String description, BigDecimal amount) {
        var text = description != null ? description : "";
        var transaction = new Transaction(TransactionType.DEPOSIT, text, amount);
        return transactions.save(transaction);
    }

    @Override
    public Transaction withdraw( String description, BigDecimal amount) {
        var text = description != null ? description : "";
        var transaction = new Transaction(TransactionType.WITHDRAW, text, amount);
        return transactions.save(transaction);
    }


}
