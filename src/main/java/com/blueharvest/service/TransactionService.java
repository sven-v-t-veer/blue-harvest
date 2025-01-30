package com.blueharvest.service;

import com.blueharvest.spi.Transaction;

import java.math.BigDecimal;

public interface TransactionService {
    Transaction deposit(String description, BigDecimal amount);
    Transaction withdraw(String description, BigDecimal amount);
}
