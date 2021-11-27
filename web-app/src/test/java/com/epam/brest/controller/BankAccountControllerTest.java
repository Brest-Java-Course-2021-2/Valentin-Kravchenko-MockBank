package com.epam.brest.controller;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

class BankAccountControllerTest extends BasicControllerTest {

    private final BankAccountService bankAccountService;

    public BankAccountControllerTest(@Autowired BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Test
    void getCreate() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("account"))
                .andExpect(model().attribute("account",
                                              hasProperty("registrationDate", is(LocalDate.now()))));

    }

    @Test
    void getUpdate() throws Exception {
        BankAccount bankAccountFromDb = bankAccountService.getById(1);
        mockMvc.perform(get("/account/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("text/html;charset=UTF-8"))
               .andExpect(view().name("account"))
               .andExpect(model().attribute("account", bankAccountFromDb));
    }

    @Test
    void create() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("customer", "New Customer");
        mockMvc.perform(post("/account").params(params))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/accounts"))
               .andExpect(redirectedUrl("/accounts"))
               .andExpect(flash().attributeExists("message"));
    }

    @Test
    void updateSucceeded() throws Exception {
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
    void updateWithInvalidCustomer() throws Exception {
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
    void updateWithInvalidId() throws Exception {
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
        BankAccount newBankAccount = bankAccountService.create(bankAccount);
        String urlTemplate = "/account/" + newBankAccount.getId() + "/remove";
        mockMvc.perform(post(urlTemplate))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/accounts"))
               .andExpect(redirectedUrl("/accounts"))
               .andExpect(flash().attributeExists("message"));
        assertThrows(IllegalArgumentException.class, () -> bankAccountService.delete(newBankAccount));
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