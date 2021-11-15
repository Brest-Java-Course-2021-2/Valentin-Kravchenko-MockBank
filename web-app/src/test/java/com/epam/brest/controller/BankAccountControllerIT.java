package com.epam.brest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

class BankAccountControllerIT extends BasicControllerTest {

    private final WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    BankAccountControllerIT(@Autowired WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void accounts() {
        
    }

    @Test
    void getAccount() {
    }

    @Test
    void testGetAccount() {
    }

    @Test
    void postAccount() {
    }

    @Test
    void testPostAccount() {
    }
}