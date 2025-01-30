package com.blueharvest.config;

import com.blueharvest.domain.AccountDto;
import com.blueharvest.domain.CustomerDto;
import com.blueharvest.domain.TransactionDto;
import com.blueharvest.spi.Account;
import com.blueharvest.spi.Customer;
import com.blueharvest.spi.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Instant;

@Configuration
@EnableJpaRepositories(basePackages = "com.blueharvest.spi.repository")
@EnableTransactionManagement
public class ServiceConfiguration {

    @Bean
    public ObjectMapper mapper() {
        var module = new JavaTimeModule();
        module.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        var mapper = JsonMapper.builder()
                .addModule(module)
                .build();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public TypeMap<Customer, CustomerDto> customerDataMapper() {
        return modelMapper().createTypeMap(Customer.class, CustomerDto.class);
    }

    @Bean
    public TypeMap<Account, AccountDto> accountDataMapper() {
        return modelMapper().createTypeMap(Account.class, AccountDto.class);
    }

    @Bean
    public TypeMap<Transaction, TransactionDto> transactionDataMapper() {
        return modelMapper().createTypeMap(Transaction.class, TransactionDto.class);
    }
}
