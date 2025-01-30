package com.greengrass.service;

import com.greengrass.exception.AccountNotFoundException;
import com.greengrass.exception.CustomerNotFoundException;
import com.greengrass.exception.InsufficientFundsException;
import com.greengrass.spi.Account;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountService {

    Account createAccount(UUID customerId, BigDecimal initialBalance) throws CustomerNotFoundException, AccountNotFoundException;
    Account deposit(UUID customerId, UUID accountId, String description, BigDecimal amount) throws CustomerNotFoundException, AccountNotFoundException;
    Account withdraw(UUID customerId, UUID accountId, String description, BigDecimal amount) throws CustomerNotFoundException, AccountNotFoundException, InsufficientFundsException;
}
