package com.greengrass.exception;

import java.util.UUID;

public class AccountNotFoundException extends Exception {
    private final UUID accountId;
    public AccountNotFoundException(UUID customerId) {
        this.accountId = customerId;
    }

    @Override
    public String getMessage() {
        return "Unable to find an account with id: " + accountId.toString();
    }
}
