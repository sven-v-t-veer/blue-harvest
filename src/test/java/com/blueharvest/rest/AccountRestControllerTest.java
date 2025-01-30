package com.blueharvest.rest;

import com.blueharvest.BlueHarvestApp;
import com.blueharvest.domain.AccountDto;
import com.blueharvest.domain.CustomerDto;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = {"com.blueharvest"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = BlueHarvestApp.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = BlueHarvestApp.class)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class AccountRestControllerTest implements MvcResponseParser{

    @Autowired
    private MockMvc mvc;


    @SneakyThrows
    @Test
    void testAccountRestController() {
        // we need a customer
        var result = mvc.perform(
                post("/api/customer/?userName=sven")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        var response = parseResponse(result, CustomerDto.class);
        Assertions.assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "sven")
                .hasFieldOrProperty("customerId");
        var customerId = response.getCustomerId();
        result = mvc.perform(
                        post("/api/customer/" + customerId.toString() + "/account/?initialCredit=100.50")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        var account = parseResponse(result, AccountDto.class);
        Assertions.assertThat(account)
                .isNotNull()
                .hasFieldOrPropertyWithValue("balance", BigDecimal.valueOf(100.50).setScale(2));
        var transactions = account.getTransactions();
        Assertions.assertThat(transactions)
                .isNotNull()
                .hasSize(1);

        // use random uuid
        mvc.perform(
                post("/api/customer/50146069-315d-47b8-8759-d1a1e5d510e4/account/?initialCredit=100.50")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        // bad request
        mvc.perform(
                post("/api/customer/not-a-uuid/account/?initialCredit=100.50")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // bad request
        var url = "/api/customer/" + customerId + "/account/?initialCredit=not_a_value";
        System.out.println(url);
        mvc.perform(
                post("/api/customer/" + customerId + "/account/?initialCredit=not_a_value")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}
