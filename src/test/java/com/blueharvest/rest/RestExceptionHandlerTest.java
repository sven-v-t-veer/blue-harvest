package com.blueharvest.rest;

import com.blueharvest.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    public static final String CONTEXT = "Http://some.context.path";
    public static final String MESSAGE = "this is the exception message";

    @Spy
    private ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Mock
    private WebRequest request;

    @InjectMocks
    private RestExceptionHandler handler;

    @Test
    void testInternalError() {
        when(request.getContextPath()).thenReturn(CONTEXT);
        var result = handler.handleInternalError(new Exception(MESSAGE), request);
        var error = (ApiError)result.getBody();
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getStatus());
        Assertions.assertEquals(MESSAGE, error.getMessage());
        Assertions.assertEquals(CONTEXT, error.getPath());
        Assertions.assertNotNull(error.getTimestamp());
    }

    @Test
    void testNoContent() {
        when(request.getContextPath()).thenReturn(CONTEXT);
        var result = handler.handleNoContent(new Exception(MESSAGE), request);
        var error = (ApiError)result.getBody();
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, error.getStatus());
        Assertions.assertEquals(MESSAGE, error.getMessage());
        Assertions.assertEquals(CONTEXT, error.getPath());
        Assertions.assertNotNull(error.getTimestamp());
    }

    @Test
    void testNotFound() {
        when(request.getContextPath()).thenReturn(CONTEXT);
        var result = handler.handleNotFound(new Exception(MESSAGE), request);
        var error = (ApiError)result.getBody();
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
        Assertions.assertEquals(MESSAGE, error.getMessage());
        Assertions.assertEquals(CONTEXT, error.getPath());
        Assertions.assertNotNull(error.getTimestamp());
    }

    @Test
    void testBadRequestException() {
        when(request.getContextPath()).thenReturn(CONTEXT);
        var result = handler.handleBadRequest(new Exception(MESSAGE), request);
        var error = (ApiError)result.getBody();
        Assertions.assertNotNull(error);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
        Assertions.assertEquals(MESSAGE, error.getMessage());
        Assertions.assertEquals(CONTEXT, error.getPath());
        Assertions.assertNotNull(error.getTimestamp());
    }
}
