package com.blueharvest.exception;

import java.util.UUID;

public class InsufficientFundsException extends Exception {
    private final UUID accountId;

    public InsufficientFundsException(UUID accountId) {
        this.accountId = accountId;
    }

    @Override
    public String getMessage() {
        return "The account " + accountId + " has insufficient funds for the transaction to be completed.";
    }
}
