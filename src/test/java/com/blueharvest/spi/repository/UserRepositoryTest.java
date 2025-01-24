package com.blueharvest.spi.repository;

import com.blueharvest.BlueHarvestApp;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = {"com.blueharvest"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BlueHarvestApp.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void testReporitory() {
        Assertions.assertThat(repository.count()).isZero();
        var user = new User();
        user.setName("user");
        var saved = repository.save(user);
        Assertions.assertThat(repository.count()).isOne();
        var account = new Account("account");
        user.addAccount(account);
        saved = repository.save(user);
        Assertions.assertThat(saved)
                .isNotNull();
        var list = saved.getAccounts();
        Assertions.assertThat(list).hasSize(1);
    }
}
