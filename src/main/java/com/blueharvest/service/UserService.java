package com.blueharvest.service;

import com.blueharvest.exception.UserNotFoundException;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.User;

import java.util.UUID;

public interface UserService {

    User getUser(UUID userId) throws UserNotFoundException;
    User addAccount(UUID usedId, Account account) throws UserNotFoundException;
}
