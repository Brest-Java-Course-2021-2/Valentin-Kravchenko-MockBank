package com.epam.brest.webapp.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.exception.BankAccountException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankAccountControllerIT extends ControllerTestConfiguration {

    private final MockMvc mockMvc;
    private final BankAccountService bankAccountService;

    public BankAccountControllerIT(@Autowired MockMvc mockMvc,
                                   @Autowired BankAccountService bankAccountService) {
        this.mockMvc = mockMvc;
        this.bankAccountService = bankAccountService;
    }

    @Test
    void createGET() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("account"))
                .andExpect(model().attribute("account", hasProperty("registrationDate", is(LocalDate.now()))));

    }

    @Test
    void updateGET() throws Exception {
        BankAccount bankAccountFromDb = bankAccountService.getById(1);
        mockMvc.perform(get("/account/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("text/html;charset=UTF-8"))
               .andExpect(view().name("account"))
               .andExpect(model().attribute("account", bankAccountFromDb));
    }

    @Test
    void createPOST() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("customer", "New Customer");
        mockMvc.perform(post("/account").params(params))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/accounts"))
               .andExpect(redirectedUrl("/accounts"))
               .andExpect(flash().attributeExists("message"));
    }

    @Test
    void updatePOSTSucceeded() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("customer", "Changed Customer");
        mockMvc.perform(post("/account/1").params(params))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/accounts"))
               .andExpect(redirectedUrl("/accounts"))
               .andExpect(flash().attributeExists("message"));
        BankAccount bankAccountFromDb = bankAccountService.getById(1);
        assertEquals(bankAccountFromDb.getCustomer(), "Changed Customer");
    }

    @Test
    void updatePOSTWithInvalidCustomer() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("customer", "Changed Customer1");
        mockMvc.perform(post("/account/1").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("account"))
                .andExpect(model().attribute("account",
                                             hasProperty("customer", is("Changed Customer1"))));
    }

    @Test
    void updatePOSTWithInvalidId() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("customer", "Changed Customer");
        mockMvc.perform(post("/account/1000").params(params))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/accounts"))
               .andExpect(redirectedUrl("/accounts"))
               .andExpect(flash().attributeExists("error"));
    }

    @Test
    void deleteSucceeded() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("Customer");
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        String urlTemplate = "/account/" + createdBankAccount.getId() + "/remove";
        mockMvc.perform(post(urlTemplate))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/accounts"))
               .andExpect(redirectedUrl("/accounts"))
               .andExpect(flash().attributeExists("message"));
        assertThrows(BankAccountException.class, () -> bankAccountService.delete(createdBankAccount.getId()));
    }

    @Test
    void deleteFailed() throws Exception {
        mockMvc.perform(post("/account/1/remove"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/accounts"))
               .andExpect(redirectedUrl("/accounts"))
               .andExpect(flash().attributeExists("error"));
    }

}