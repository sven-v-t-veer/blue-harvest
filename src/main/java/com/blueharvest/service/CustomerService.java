package com.blueharvest.service;

import com.blueharvest.exception.UserNotFoundException;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.Customer;

import java.util.UUID;

public interface CustomerService {

    Customer getCustomer(UUID customerId) throws UserNotFoundException;
    Customer addAccount(UUID customerId, Account account) throws UserNotFoundException;
    Customer createUser(String name);
}
