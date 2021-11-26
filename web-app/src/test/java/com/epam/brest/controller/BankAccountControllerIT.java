package com.epam.brest.controller;

import com.epam.brest.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

class BankAccountControllerIT extends BasicControllerTest {

    private final BankAccountService bankAccountService;
    private MockMvc mockMvc;

    public BankAccountControllerIT(@Autowired BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
       mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void accounts() throws Exception {
        mockMvc.perform(get("/accounts"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("text/html;charset=UTF-8"))
               .andExpect(view().name("accounts"));
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