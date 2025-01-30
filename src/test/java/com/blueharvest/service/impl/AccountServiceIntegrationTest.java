package com.blueharvest.service.impl;

import com.blueharvest.BlueHarvestApp;
import com.blueharvest.exception.AccountNotFoundException;
import com.blueharvest.exception.CustomerNotFoundException;
import com.blueharvest.service.AccountService;
import com.blueharvest.service.CustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = {"com.blueharvest"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BlueHarvestApp.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accounts;

    @Autowired
    private CustomerService users;


    @Test
    void testCreateAccountUserNotFoundException() {
        Assertions.assertThatThrownBy(() -> accounts.createAccount(UUID.randomUUID(), BigDecimal.TEN)).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void testCreateAccountNullInitialBalance() throws CustomerNotFoundException, AccountNotFoundException {
        var user = users.createUser("test user");
        var account = accounts.createAccount(user.getCustomerId(), null);
        Assertions.assertThat(account)
                .isNotNull()
                .hasFieldOrPropertyWithValue("balance", BigDecimal.ZERO)
                .hasFieldOrProperty("accountId");
    }

    @Test
    void testCreateAccountZeroInitialBalance() throws CustomerNotFoundException, AccountNotFoundException {
        var user = users.createUser("test user");
        var account = accounts.createAccount(user.getCustomerId(), BigDecimal.ZERO);
        Assertions.assertThat(account)
                .isNotNull()
                .hasFieldOrPropertyWithValue("balance", BigDecimal.ZERO)
                .hasFieldOrProperty("accountId");
    }

    @Test
    void testCreateAccountTenInitialBalance() throws CustomerNotFoundException, AccountNotFoundException {
        var user = users.createUser("test user");
        var account = accounts.createAccount(user.getCustomerId(), BigDecimal.TEN);
        Assertions.assertThat(account)
                .isNotNull()
                .hasFieldOrPropertyWithValue("balance", BigDecimal.TEN)
                .hasFieldOrProperty("accountId");
        Assertions.assertThat(account.getTransactions()).hasSize(1);
        var transaction = account.getTransactions().getFirst();
        Assertions.assertThat(transaction)
                .isNotNull()
                .hasFieldOrPropertyWithValue("amount", BigDecimal.TEN);
    }
}
