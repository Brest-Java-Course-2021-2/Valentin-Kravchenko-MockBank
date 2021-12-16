package com.epam.brest.restapp.controller;

import com.epam.brest.restapp.RestApp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(classes = {RestApp.class})
@AutoConfigureMockMvc
@Transactional
public class RestControllerTestBasic {

    private final ObjectMapper objectMapper;

    public RestControllerTestBasic(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    MockHttpServletRequestBuilder doPost(String uri, Object body) {
        try {
            String json = objectMapper.writeValueAsString(body);
            return post(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    MockHttpServletRequestBuilder doPut(String uri, Object body) {
        try {
            String json = objectMapper.writeValueAsString(body);
            return put(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
