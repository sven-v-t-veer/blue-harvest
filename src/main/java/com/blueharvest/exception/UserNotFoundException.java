package com.blueharvest.exception;

import java.util.UUID;

public class UserNotFoundException extends Exception {
    private final UUID userId;
    public UserNotFoundException(UUID userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return "Unable to find a user with id: " + userId.toString();
    }
}
