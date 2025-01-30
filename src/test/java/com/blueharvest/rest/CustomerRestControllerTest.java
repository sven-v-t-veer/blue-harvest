package com.blueharvest.rest;

import com.blueharvest.BlueHarvestApp;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class CustomerRestControllerTest implements MvcResponseParser{

    @Autowired
    private MockMvc mvc;


    @SneakyThrows
    @Test
    void testCustomerRestController() {
        var result = mvc.perform(
                post("/api/customer/?name=sven&surName=Veer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        var response = parseResponse(result, CustomerDto.class);
        Assertions.assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "sven")
                .hasFieldOrPropertyWithValue("surName", "Veer")
                .hasFieldOrProperty("customerId");
        var customerId = response.getCustomerId();
        result = mvc.perform(
                        get("/api/customer/" + customerId.toString())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        var r2 = parseResponse(result, CustomerDto.class);
        Assertions.assertThat(r2)
                .isNotNull()
                .isEqualTo(response);

        // uuid unknown
        mvc.perform(
                get("/api/customer/50146069-315d-47b8-8759-d1a1e5d510e4")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // not a uuid
        mvc.perform(
                get("/api/customer/not_a_uuid")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}
