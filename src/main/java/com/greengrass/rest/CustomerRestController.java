package com.greengrass.rest;

import com.greengrass.domain.CustomerDto;
import com.greengrass.exception.CustomerNotFoundException;
import com.greengrass.service.CustomerService;
import com.greengrass.spi.Customer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@Transactional
public class CustomerRestController {

    private final CustomerService customers;
    private final TypeMap<Customer, CustomerDto> customerDataMapper;
    private final RestExceptionHandler handler;

    public CustomerRestController(CustomerService customers, TypeMap<Customer, CustomerDto> customerDataMapper, RestExceptionHandler handler) {
        this.customers = customers;
        this.customerDataMapper = customerDataMapper;
        this.handler = handler;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping({"/customer/", "/customer"})
    CustomerDto createCustomer(@RequestParam(name = "name") String name, @RequestParam(name = "surname") String surname) {
        return customerDataMapper.map(customers.createCustomer(name, surname));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/customer/{customerId}/", "/customer/{customerId}"})
    CustomerDto getCustomer(@PathVariable(name = "customerId") UUID customerId) throws CustomerNotFoundException {
        return customerDataMapper.map(customers.getCustomer(customerId));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(
            Exception ex, WebRequest request) {
        log.warn("Handling NotFound Exception", ex);
        return handler.handleNotFound(ex, request);
    }
}
