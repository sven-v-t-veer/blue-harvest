package com.blueharvest.rest;

import com.blueharvest.domain.AccountDto;
import com.blueharvest.exception.AccountNotFoundException;
import com.blueharvest.exception.CustomerNotFoundException;
import com.blueharvest.exception.InsufficientFundsException;
import com.blueharvest.service.AccountService;
import com.blueharvest.spi.Account;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@Transactional
public class AccountRestController {

    private final AccountService accounts;
    private final TypeMap<Account, AccountDto> accountDataMapper;
    private final RestExceptionHandler handler;

    public AccountRestController(AccountService accounts, TypeMap<Account, AccountDto> accountDataMapper, RestExceptionHandler handler) {
        this.accounts = accounts;
        this.accountDataMapper = accountDataMapper;
        this.handler = handler;
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping({"/customer/{customerId}/account/", "/customer/{customerId}/account"})
    AccountDto createAccount(@PathVariable(name = "customerId") UUID customerId, @RequestParam(name = "initialCredit", required = false)BigDecimal initialCredit) throws CustomerNotFoundException, AccountNotFoundException {
        return accountDataMapper.map(accounts.createAccount(customerId, initialCredit));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping({"/customer/{customerId}/account/{accountId}/deposit/", "/customer/{customerId}/account/{accountId}/deposit"})
    AccountDto deposit(@PathVariable(name = "customerId") UUID customerId, @PathVariable(name = "accountId") UUID accountId,
                       @RequestParam(name = "description", required = false) String description,
                       @RequestParam(name = "amount") BigDecimal amount)
            throws CustomerNotFoundException, AccountNotFoundException {
        return accountDataMapper.map(accounts.deposit(customerId, accountId, description, amount));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping({"/customer/{customerId}/account/{accountId}/withdraw/", "/customer/{customerId}/account/{accountId}/withdraw"})
    AccountDto withdraw(@PathVariable(name = "customerId") UUID customerId, @PathVariable(name = "accountId") UUID accountId,
                       @RequestParam(name = "description", required = false) String description,
                       @RequestParam(name = "amount") BigDecimal amount)
            throws CustomerNotFoundException, AccountNotFoundException, InsufficientFundsException {
        return accountDataMapper.map(accounts.withdraw(customerId, accountId, description, amount));
    }

    @ExceptionHandler({CustomerNotFoundException.class, AccountNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(
            Exception ex, WebRequest request) {
        log.warn("Handling NotFound Exception", ex);
        return handler.handleNotFound(ex, request);
    }

    @ExceptionHandler({InsufficientFundsException.class})
    public ResponseEntity<Object> handleBadRequest(
            Exception ex, WebRequest request) {
        log.warn("Handling InsufficientFunds Exception", ex);
        return handler.handleBadRequest(ex, request);
    }

}
