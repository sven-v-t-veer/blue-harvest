package com.blueharvest.service.impl;

import com.blueharvest.exception.AccountNotFoundException;
import com.blueharvest.exception.UserNotFoundException;
import com.blueharvest.service.AccountService;
import com.blueharvest.service.CustomerService;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.Transaction;
import com.blueharvest.spi.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Transactional
@Component
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accounts;
    private final CustomerService users;

    public AccountServiceImpl(AccountRepository accounts, CustomerService users) {
        this.accounts = accounts;
        this.users = users;
    }

    @Override
    public Account createAccount(UUID customerId, BigDecimal initialBalance) throws UserNotFoundException, AccountNotFoundException {
        var account = accounts.save(new Account(initialBalance));
        var accountId = account.getAccountId();
        if (initialBalance != null && !initialBalance.equals(BigDecimal.ZERO)) {
            account.addTransaction(new Transaction("initial balance", initialBalance));
            account = accounts.save(account);
        }
        var user = users.addAccount(customerId, account);
        return user.getAccounts()
                .stream()
                .filter(a -> a.getAccountId().equals(accountId))
                .findAny()
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
