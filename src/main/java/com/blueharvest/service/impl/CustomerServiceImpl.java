package com.blueharvest.service.impl;

import com.blueharvest.exception.UserNotFoundException;
import com.blueharvest.service.CustomerService;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.Customer;
import com.blueharvest.spi.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Component
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository users;

    public CustomerServiceImpl(CustomerRepository users) {
        this.users = users;
    }

    @Override
    public Customer createUser(String name) {
        return users.save(new Customer(name));
    }

    @Override
    public Customer getCustomer(UUID customerId) throws UserNotFoundException {
        return users.findById(customerId).orElseThrow(() -> new UserNotFoundException(customerId));
    }

    @Override
    public Customer addAccount(UUID customerId, Account account) throws UserNotFoundException {
        var user = getCustomer(customerId);
        user.addAccount(account);
        return users.save(user);
    }
}
