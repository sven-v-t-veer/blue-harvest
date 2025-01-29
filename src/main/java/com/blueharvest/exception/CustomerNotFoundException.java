package com.blueharvest.exception;

import java.util.UUID;

public class CustomerNotFoundException extends Exception {
    private final UUID customerId;
    public CustomerNotFoundException(UUID customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return "Unable to find a user with id: " + customerId.toString();
    }
}
