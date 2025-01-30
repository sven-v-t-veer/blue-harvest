package com.blueharvest.rest;

import com.blueharvest.exception.ApiError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@Slf4j
@Component
@SuppressWarnings("java:S7027")
public class RestExceptionHandler {

    private final ObjectMapper mapper;

    public RestExceptionHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
        var error = new ApiError(HttpStatus.BAD_REQUEST, request.getContextPath(), ex.getMessage(), Instant.now());
        try {
            log.warn("HttpError: Bad Request. {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(error), ex);
        } catch (JsonProcessingException e) {
            //ignore, it's just logging
        }
        return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
    }


    ResponseEntity<Object> handleInternalError(Exception ex, WebRequest request) {
        var error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, request.getContextPath(), ex.getMessage(), Instant.now());
        try {
            log.warn("HttpError: Internal Error. {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(error), ex);
        } catch (JsonProcessingException e) {
            //ignore, it's just logging
        }
        return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
    }

    public ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        var error = new ApiError(HttpStatus.NOT_FOUND, request.getContextPath(), ex.getMessage(), Instant.now());
        try {
            log.warn("HttpError: Not Found. {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(error), ex);
        } catch (JsonProcessingException e) {
            //ignore, it's just logging
        }
        return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
    }
}
