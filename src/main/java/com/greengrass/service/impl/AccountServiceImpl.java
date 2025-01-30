package com.greengrass.service.impl;

import com.greengrass.domain.TransactionType;
import com.greengrass.exception.AccountNotFoundException;
import com.greengrass.exception.CustomerNotFoundException;
import com.greengrass.exception.InsufficientFundsException;
import com.greengrass.service.AccountService;
import com.greengrass.service.CustomerService;
import com.greengrass.service.TransactionService;
import com.greengrass.spi.Account;
import com.greengrass.spi.Transaction;
import com.greengrass.spi.repository.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Transactional
@Component
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accounts;
    private final CustomerService users;
    private final TransactionService transactions;

    public AccountServiceImpl(AccountRepository accounts, CustomerService users, TransactionService transactions) {
        this.accounts = accounts;
        this.users = users;
        this.transactions = transactions;
    }

    @Override
    public Account createAccount(UUID customerId, BigDecimal initialBalance) throws CustomerNotFoundException, AccountNotFoundException {
        var account = accounts.save(new Account(initialBalance));
        var accountId = account.getAccountId();
        if (initialBalance != null && !initialBalance.equals(BigDecimal.ZERO)) {
            account.addTransaction(new Transaction(TransactionType.DEPOSIT, "initial balance", initialBalance));
            account = accounts.save(account);
        }
        var user = users.addAccount(customerId, account);
        return user.getAccounts()
                .stream()
                .filter(a -> a.getAccountId().equals(accountId))
                .findAny()
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Override
    public Account deposit(UUID customerId, UUID accountId, String description, BigDecimal amount) throws CustomerNotFoundException, AccountNotFoundException {
        var account = getAccount(customerId, accountId);
        account.addTransaction(transactions.deposit(description, amount));
        account.setBalance(account.getBalance().add(amount));
        return accounts.save(account);
    }

    @Override
    public Account withdraw(UUID customerId, UUID accountId, String description, BigDecimal amount) throws CustomerNotFoundException, AccountNotFoundException, InsufficientFundsException {
        var account = getAccount(customerId, accountId);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountId);
        }
        account.addTransaction(transactions.withdraw(description, amount));
        account.setBalance(account.getBalance().subtract(amount));
        return accounts.save(account);
    }

    Account getAccount(UUID customerId, UUID accountId) throws CustomerNotFoundException, AccountNotFoundException {
        return users.getCustomer(customerId)
                .getAccounts()
                .stream()
                .filter(a -> a.getAccountId().equals(accountId))
                .findAny()
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }


}
