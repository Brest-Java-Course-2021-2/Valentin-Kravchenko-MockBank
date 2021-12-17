package com.epam.brest.restapp.controller;

import com.epam.brest.restapp.RestApp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {RestApp.class})
@AutoConfigureMockMvc
@Transactional
public class RestControllerTestBasic {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public RestControllerTestBasic(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    ResultActions performGetAndExpectStatus(String endpoint, ResultMatcher status) throws Exception {
        return mockMvc.perform(get(endpoint))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status);
    }

    ResultActions performGetAndExpectStatusOk(String endpoint) throws Exception {
        return performGetAndExpectStatus(endpoint, status().isOk());
    }

    ResultActions performPostAndExpectStatus(String endpoint, Object body, ResultMatcher status) throws Exception {
        return mockMvc.perform(doPost(endpoint, body))
                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status);
    }

    ResultActions performPostAndExpectStatusOk(String endpoint, Object body) throws Exception {
        return performPostAndExpectStatus(endpoint, body, status().isOk());
    }

    ResultActions performPutAndExpectStatusOk(String endpoint, Object body) throws Exception {
        return mockMvc.perform(doPut(endpoint, body))
                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status().isOk());
    }

    ResultActions performDeleteAndExpectStatus(String endpoint, ResultMatcher status) throws Exception {
        return mockMvc.perform(delete(endpoint))
                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status);
    }

    ResultActions performDeleteAndExpectStatusOk(String endpoint) throws Exception {
        return performDeleteAndExpectStatus(endpoint, status().isOk());
    }

    private MockHttpServletRequestBuilder doPost(String uri, Object body) {
        try {
            String json = objectMapper.writeValueAsString(body);
            return post(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private MockHttpServletRequestBuilder doPut(String uri, Object body) {
        try {
            String json = objectMapper.writeValueAsString(body);
            return put(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
