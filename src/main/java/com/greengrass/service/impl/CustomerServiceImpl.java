package com.greengrass.service.impl;

import com.greengrass.exception.CustomerNotFoundException;
import com.greengrass.service.CustomerService;
import com.greengrass.spi.Account;
import com.greengrass.spi.Customer;
import com.greengrass.spi.repository.CustomerRepository;
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
    public Customer createCustomer(String name, String surname) {
        return users.save(new Customer(name, surname));
    }

    @Override
    public Customer getCustomer(UUID customerId) throws CustomerNotFoundException {
        return users.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Override
    public Customer addAccount(UUID customerId, Account account) throws CustomerNotFoundException {
        var user = getCustomer(customerId);
        user.addAccount(account);
        return users.save(user);
    }
}
