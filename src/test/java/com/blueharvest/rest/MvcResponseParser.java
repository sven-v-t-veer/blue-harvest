package com.blueharvest.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.time.Instant;

public interface MvcResponseParser {

    ObjectMapper mapper = mapper();

    static ObjectMapper mapper() {
        var module = new JavaTimeModule();
        module.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        var mapper = JsonMapper.builder()
                .addModule(module)
                .build();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    default <T> T parseResponse(MvcResult result, Class<T> responseClass) {
        try {
            String contentAsString = result.getResponse().getContentAsString();
            return mapper.readValue(contentAsString, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
