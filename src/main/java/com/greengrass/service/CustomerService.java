package com.greengrass.service;

import com.greengrass.exception.CustomerNotFoundException;
import com.greengrass.spi.Account;
import com.greengrass.spi.Customer;

import java.util.UUID;

public interface CustomerService {

    Customer getCustomer(UUID customerId) throws CustomerNotFoundException;
    Customer addAccount(UUID customerId, Account account) throws CustomerNotFoundException;
    Customer createCustomer(String name, String surname);
}
