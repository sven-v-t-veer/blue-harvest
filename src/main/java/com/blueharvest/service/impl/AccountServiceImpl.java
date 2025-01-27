package com.blueharvest.service.impl;

import com.blueharvest.exception.AccountNotFoundException;
import com.blueharvest.exception.UserNotFoundException;
import com.blueharvest.service.AccountService;
import com.blueharvest.service.UserService;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Transactional
@Component
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accounts;
    private final UserService users;

    public AccountServiceImpl(AccountRepository accounts, UserService users) {
        this.accounts = accounts;
        this.users = users;
    }

    @Override
    public Account createAccount(UUID userId, BigDecimal initialBalance) throws UserNotFoundException, AccountNotFoundException {
        var account = accounts.save(new Account());
        var user = users.addAccount(userId, account);
        return user.getAccounts()
                .stream()
                .filter(a -> a.getAccountId().equals(account.getAccountId()))
                .findAny()
                .orElseThrow(() -> new AccountNotFoundException(account.getAccountId()));
    }
}
