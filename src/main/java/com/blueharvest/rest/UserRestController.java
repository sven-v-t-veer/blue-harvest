package com.blueharvest.rest;

import com.blueharvest.domain.CustomerDto;
import com.blueharvest.exception.UserNotFoundException;
import com.blueharvest.service.CustomerService;
import com.blueharvest.spi.Customer;
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
public class UserRestController {

    private final CustomerService users;
    private final TypeMap<Customer, CustomerDto> userDataMapper;
    private final RestExceptionHandler handler;

    public UserRestController(CustomerService users, TypeMap<Customer, CustomerDto> userDataMapper, RestExceptionHandler handler) {
        this.users = users;
        this.userDataMapper = userDataMapper;
        this.handler = handler;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping({"/customer/", "/customer"})
    CustomerDto createUser(@RequestParam(name = "user_name") String userName) {
        return userDataMapper.map(users.createUser(userName));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/user/{customer_id}/", "/customer/{customer_id}"})
    CustomerDto getUser(@PathVariable(name = "user_id") UUID customerId) throws UserNotFoundException {
        return userDataMapper.map(users.getCustomer(customerId));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(
            Exception ex, WebRequest request) {
        log.warn("Handling NotFound Exception", ex);
        return handler.handleNotFound(ex, request);
    }
}
