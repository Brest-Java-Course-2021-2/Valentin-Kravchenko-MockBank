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

import static com.epam.brest.webapp.constant.ControllerConstant.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class BankAccountControllerIT extends ControllerTestBasic {

    public static final String ACCOUNT_ENDPOINT = "/account";
    public static final String ACCOUNT_TEST_ID = "/account/1";
    public static final String ACCOUNT_TEST_ID_NON_EXISTS = "/account/1000";
    public static final String ACCOUNT_TEST_ID_REMOVE = "/account/1/remove";
    public static final String REGISTRATION_DATE = "registrationDate";
    public static final String NEW_CUSTOMER = "New Customer";

    private final BankAccountService bankAccountService;

    public BankAccountControllerIT(@Autowired MockMvc mockMvc,
                                   @Autowired BankAccountService bankAccountServiceRest) {
        super(mockMvc);
        this.bankAccountService = bankAccountServiceRest;
    }

    @Test
    void createGET() throws Exception {
        performGetAndExpectStatusOk(ACCOUNT_ENDPOINT, ACCOUNT)
                .andExpect(model().attribute(ACCOUNT, hasProperty(REGISTRATION_DATE, is(LocalDate.now()))));
    }

    @Test
    void updateGET() throws Exception {
        BankAccount bankAccountFromDb = bankAccountService.getById(1);
        performGetAndExpectStatusOk(ACCOUNT_TEST_ID, ACCOUNT)
               .andExpect(model().attribute(ACCOUNT, bankAccountFromDb));
    }

    @Test
    void createPOST() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("customer", "New Customer");
        performPostAndExpectStatus3xxRedirection(ACCOUNT_ENDPOINT, params, REDIRECT_ACCOUNTS, ACCOUNTS_ENDPOINT)
               .andExpect(flash().attributeExists(MESSAGE));
    }

    @Test
    void succeededUpdatePOST() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(CUSTOMER, NEW_CUSTOMER);
        performPostAndExpectStatus3xxRedirection(ACCOUNT_TEST_ID, params, REDIRECT_ACCOUNTS, ACCOUNTS_ENDPOINT)
               .andExpect(flash().attributeExists(MESSAGE));
        BankAccount bankAccountFromDb = bankAccountService.getById(1);
        assertEquals(bankAccountFromDb.getCustomer(), NEW_CUSTOMER);
    }

    @Test
    void updatePOSTWithInvalidCustomer() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(CUSTOMER, NEW_CUSTOMER + 1);
        performPostAndExpectStatusOk(ACCOUNT_TEST_ID, params, ACCOUNT)
                .andExpect(model().attribute(ACCOUNT, hasProperty(CUSTOMER, is(NEW_CUSTOMER + 1))));
    }

    @Test
    void updatePOSTWithInvalidId() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(CUSTOMER, NEW_CUSTOMER);
        performPostAndExpectStatus3xxRedirection(ACCOUNT_TEST_ID_NON_EXISTS, params, REDIRECT_ACCOUNTS, ACCOUNTS_ENDPOINT)
               .andExpect(flash().attributeExists(ERROR));
    }

    @Test
    void remove() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer(NEW_CUSTOMER);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        String endpoint = "/account/" + createdBankAccount.getId() + "/remove";
        performPostAndExpectStatus3xxRedirection(endpoint, new LinkedMultiValueMap<>(), REDIRECT_ACCOUNTS, ACCOUNTS_ENDPOINT)
               .andExpect(flash().attributeExists(MESSAGE));
        assertThrows(BankAccountException.class, () -> bankAccountService.delete(createdBankAccount.getId()));
    }

    @Test
    void failedRemove() throws Exception {
        performPostAndExpectStatus3xxRedirection(ACCOUNT_TEST_ID_REMOVE, new LinkedMultiValueMap<>(), REDIRECT_ACCOUNTS, ACCOUNTS_ENDPOINT)
               .andExpect(flash().attributeExists(ERROR));
    }

}