package com.epam.brest.restapp.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankAccountControllerIT extends RestControllerTestConfiguration {

    private final MockMvc mockMvc;
    private final BankAccountService bankAccountService;

    public BankAccountControllerIT(@Autowired MockMvc mockMvc,
                                   @Autowired ObjectMapper objectMapper,
                                   @Autowired BankAccountService bankAccountService) {
        super(objectMapper);
        this.mockMvc = mockMvc;
        this.bankAccountService = bankAccountService;
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account"))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    void getById() throws Exception {
        BankAccount bankAccount = bankAccountService.getById(1);
        mockMvc.perform(MockMvcRequestBuilders.get("/account/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bankAccount.getId())))
                .andExpect(jsonPath("$.customer", is(bankAccount.getCustomer())));
    }

    @Test
    void getByNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/100"))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value(containsString("100")));
    }

    @Test
    void getByInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/account"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("account")));
    }

    @Test
    void getByIdInvalidEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/account/1/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource Not Found"));
    }

    @Test
    void createSucceeded() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("New Customer");
        mockMvc.perform(doPost("/account", bankAccount))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.customer", is("New Customer")));
    }

    @Test
    void createFailed() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("New Customer1");
        mockMvc.perform(doPost("/account", bankAccount))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.validationErrors.customer").value("Customer full name is incorrect!"));

    }

    @Test
    void update() throws Exception {
        BankAccount bankAccount = bankAccountService.getById(1);
        bankAccount.setCustomer("New Customer");
        mockMvc.perform(doPut("/account", bankAccount))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bankAccount.getId())))
                .andExpect(jsonPath("$.customer", is("New Customer")));
    }

    @Test
    void delete() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("New Customer");
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        mockMvc.perform(MockMvcRequestBuilders.delete("/account/" + createdBankAccount.getId()))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", is(createdBankAccount.getId())))
               .andExpect(status().isOk());

    }

}