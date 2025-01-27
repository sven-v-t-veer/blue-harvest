package com.blueharvest.service.impl;

import com.blueharvest.exception.UserNotFoundException;
import com.blueharvest.service.UserService;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.User;
import com.blueharvest.spi.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Component
public class UserServiceImpl implements UserService {

    private final UserRepository users;

    public UserServiceImpl(UserRepository users) {
        this.users = users;
    }

    @Override
    public User createUser(String name) {
        return users.save(new User(name));
    }

    @Override
    public User getUser(UUID userId) throws UserNotFoundException {
        return users.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public User addAccount(UUID userId, Account account) throws UserNotFoundException {
        var user = getUser(userId);
        user.addAccount(account);
        return users.save(user);
    }
}
