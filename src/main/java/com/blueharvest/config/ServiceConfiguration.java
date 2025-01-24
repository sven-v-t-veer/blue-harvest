package com.blueharvest.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "com.blueharvest.spi.repository")
@EnableTransactionManagement
public class ServiceConfiguration {
}
