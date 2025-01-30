package com.blueharvest.spi.repository;

import com.blueharvest.BlueHarvestApp;
import com.blueharvest.domain.TransactionType;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.Transaction;
import com.blueharvest.spi.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = {"com.blueharvest"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BlueHarvestApp.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customers;
    @Autowired
    private AccountRepository accounts;

    @BeforeEach
    void setup() {
        customers.deleteAll();
        Assertions.assertThat(customers.count()).isZero();
    }

    @Test
    void testRepository() {
        Assertions.assertThat(customers.count()).isZero();
        var customer = new Customer("name", "surname");
        customers.save(customer);
        Assertions.assertThat(customers.count()).isOne();
        var account = new Account();
        account.setBalance(BigDecimal.TEN);
        customer.addAccount(account);
        var saved = customers.save(customer);
        Assertions.assertThat(saved)
                .isNotNull();
        List<Account> list = saved.getAccounts();
        Assertions.assertThat(list).hasSize(1);
        account = list.getFirst();
        Assertions.assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);
        var balance = account.addTransaction(new Transaction(TransactionType.DEPOSIT, "description", BigDecimal.ONE));
        Assertions.assertThat(balance).isEqualTo(new BigDecimal(10));
        account = accounts.save(account);
        Assertions.assertThat(account.getBalance()).isEqualTo(new BigDecimal(10));
    }

}
