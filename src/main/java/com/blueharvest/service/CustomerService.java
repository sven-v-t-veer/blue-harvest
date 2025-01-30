package com.blueharvest.service;

import com.blueharvest.exception.CustomerNotFoundException;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.Customer;

import java.util.UUID;

public interface CustomerService {

    Customer getCustomer(UUID customerId) throws CustomerNotFoundException;
    Customer addAccount(UUID customerId, Account account) throws CustomerNotFoundException;
    Customer createCustomer(String name, String surname);
}
