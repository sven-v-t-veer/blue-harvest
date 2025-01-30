package com.greengrass.service.impl;

import com.greengrass.GreenGrassApp;
import com.greengrass.exception.AccountNotFoundException;
import com.greengrass.exception.CustomerNotFoundException;
import com.greengrass.exception.InsufficientFundsException;
import com.greengrass.service.AccountService;
import com.greengrass.service.CustomerService;
import lombok.SneakyThrows;
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
@ComponentScan(basePackages = {"com.greengrass"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = GreenGrassApp.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accounts;

    @Autowired
    private CustomerService customers;


    @Test
    void testCreateAccountUserNotFoundException() {
        Assertions.assertThatThrownBy(() -> accounts.createAccount(UUID.randomUUID(), BigDecimal.TEN)).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void testCreateAccountNullInitialBalance() throws CustomerNotFoundException, AccountNotFoundException {
        var customer = customers.createCustomer("test user", "test user");
        var account = accounts.createAccount(customer.getCustomerId(), null);
        Assertions.assertThat(account)
                .isNotNull()
                .hasFieldOrPropertyWithValue("balance", BigDecimal.ZERO)
                .hasFieldOrProperty("accountId");
    }

    @Test
    void testCreateAccountZeroInitialBalance() throws CustomerNotFoundException, AccountNotFoundException {
        var customer = customers.createCustomer("test user", "test user");
        var account = accounts.createAccount(customer.getCustomerId(), BigDecimal.ZERO);
        Assertions.assertThat(account)
                .isNotNull()
                .hasFieldOrPropertyWithValue("balance", BigDecimal.ZERO)
                .hasFieldOrProperty("accountId");
    }


    @Test
    void testCreateAccountTenInitialBalance() throws CustomerNotFoundException, AccountNotFoundException {
        var customer = customers.createCustomer("test user", "test user");
        var account = accounts.createAccount(customer.getCustomerId(), BigDecimal.TEN);
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

    @SneakyThrows
    @Test
    void testCreateAccountTenInitialBalanceWithdraw1Deposit1Withdraw10WithdrawToThrow() {
        var customer = customers.createCustomer("test user", "test user");
        var customerId = customer.getCustomerId();
        var account = accounts.createAccount(customerId, BigDecimal.TEN);
        var accountId = account.getAccountId();
        Assertions.assertThat(account)
                .isNotNull()
                .hasFieldOrPropertyWithValue("balance", BigDecimal.TEN)
                .hasFieldOrProperty("accountId");
        Assertions.assertThat(account.getTransactions()).hasSize(1);
        var transaction = account.getTransactions().getFirst();
        Assertions.assertThat(transaction)
                .isNotNull()
                .hasFieldOrPropertyWithValue("amount", BigDecimal.TEN);
        account = accounts.withdraw(customerId, accountId, "withdraw", BigDecimal.ONE);
        Assertions.assertThat(account.getTransactions()).hasSize(2);
        Assertions.assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(9L));
        account = accounts.deposit(customerId, accountId, "deposit", BigDecimal.ONE);
        Assertions.assertThat(account.getTransactions()).hasSize(3);
        Assertions.assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);
        account = accounts.withdraw(customerId, accountId, "withdraw", BigDecimal.TEN);
        Assertions.assertThat(account.getTransactions()).hasSize(4);
        Assertions.assertThat(account.getBalance()).isEqualTo(BigDecimal.ZERO);
        Assertions.assertThatThrownBy(() -> accounts.withdraw(customerId, accountId, "withdraw", BigDecimal.TEN)).isInstanceOf(InsufficientFundsException.class);
    }
}
