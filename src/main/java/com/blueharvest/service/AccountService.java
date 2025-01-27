package com.blueharvest.service;

import com.blueharvest.exception.AccountNotFoundException;
import com.blueharvest.exception.UserNotFoundException;
import com.blueharvest.spi.Account;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountService {

    Account createAccount(UUID userId, BigDecimal initialBalance) throws UserNotFoundException, AccountNotFoundException;
}
