package com.epam.brest.restapp.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.api.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankAccountControllerIT extends RestControllerTestBasic {

    private final BankAccountService bankAccountService;

    public BankAccountControllerIT(@Autowired MockMvc mockMvc,
                                   @Autowired ObjectMapper objectMapper,
                                   @Autowired BankAccountService bankAccountService) {
        super(mockMvc, objectMapper);
        this.bankAccountService = bankAccountService;
    }

    @Test
    void get() throws Exception {
        performGetAndExpectStatusOk("/account")
                .andExpect(jsonPath("$.registrationDate", is(LocalDate.now().toString())));
    }

    @Test
    void getById() throws Exception {
        BankAccount bankAccount = bankAccountService.getById(1);
        performGetAndExpectStatusOk("/account/1")
                .andExpect(jsonPath("$.id", is(bankAccount.getId())))
                .andExpect(jsonPath("$.customer", is(bankAccount.getCustomer())));
    }

    @Test
    void getByNonExistingId() throws Exception {
        performGetAndExpectStatus("/account/100", status().isBadRequest())
               .andExpect(jsonPath("$.message").value(containsString("100")));
    }

    @Test
    void getByIncorrectId() throws Exception {
        performGetAndExpectStatus("/account/one", status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("one")));
    }

    @Test
    void getByIdInvalidEndpoint() throws Exception {
        performGetAndExpectStatus("/account/1/1", status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource Not Found"));
    }

    @Test
    void create() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("customer", "New Customer");
        performPostAndExpectStatusOk("/account", body)
               .andExpect(jsonPath("$.customer", is("New Customer")));
    }

    @Test
    void failedCreate() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("customer", "New Customer1");
        performPostAndExpectStatus("/account", body, status().isBadRequest())
               .andExpect(jsonPath("$.validationErrors.customer").value("Customer full name is incorrect!"));

    }

    @Test
    void update() throws Exception {
        BankAccount bankAccount = bankAccountService.getById(1);
        bankAccount.setCustomer("New Customer");
        performPutAndExpectStatusOk("/account", bankAccount)
                .andExpect(jsonPath("$.id", is(bankAccount.getId())))
                .andExpect(jsonPath("$.customer", is("New Customer")));
    }

    @Test
    void remove() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("New Customer");
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        performDeleteAndExpectStatusOk("/account/" + createdBankAccount.getId())
               .andExpect(jsonPath("$.id", is(createdBankAccount.getId())));

    }

}