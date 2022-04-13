package com.epam.brest.restapp.controller;

import com.epam.brest.model.BankAccount;
import com.epam.brest.model.CreditCard;
import com.epam.brest.service.api.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountRestControllerIT extends RestControllerTestBasic {

    public static final String CUSTOMER = "customer";
    public static final String NEW_CUSTOMER = "New Customer";
    public static final String RESOURCE_NOT_FOUND = "Resource Not Found";
    public static final String CUSTOMER_FULL_NAME_IS_INCORRECT = "Customer full name is incorrect!";

    private final BankAccountService bankAccountService;

    public BankAccountRestControllerIT(MockMvc mockMvc,
                                       ObjectMapper objectMapper,
                                       BankAccountService bankAccountService) {
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
    void getAllCardsById() throws Exception {
        List<CreditCard> cards = bankAccountService.getAllCardsById(1);
        int lastIdx = cards.size() - 1;
        performGetAndExpectStatusOk("/account/1/cards")
                .andExpect(jsonPath("$.size()", is(cards.size())))
                .andExpect(jsonPath("$[0].accountId", is(1)))
                .andExpect(jsonPath("$[" + lastIdx + "].accountId", is(1)));
    }

    @Test
    void getByNonExistingId() throws Exception {
        performGetAndExpectStatus("/account/100", status().isNotFound())
               .andExpect(jsonPath("$.errorMessage").value(containsString("100")));
    }

    @Test
    void getByIncorrectId() throws Exception {
        performGetAndExpectStatus("/account/one", status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(containsString("one")));
    }

    @Test
    void getByIdInvalidEndpoint() throws Exception {
        performGetAndExpectStatus("/account/1/1", status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(RESOURCE_NOT_FOUND));
    }

    @Test
    void create() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put(CUSTOMER, NEW_CUSTOMER);
        performPostAndExpectStatusOk("/account", body)
               .andExpect(jsonPath("$.customer", is(NEW_CUSTOMER)));
    }

    @Test
    void failedCreate() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put(CUSTOMER, "New Customer1");
        performPostAndExpectStatus("/account", body, status().isBadRequest())
               .andExpect(jsonPath("$.validationErrors.customer").value(CUSTOMER_FULL_NAME_IS_INCORRECT));

    }

    @Test
    void update() throws Exception {
        BankAccount bankAccount = bankAccountService.getById(1);
        bankAccount.setCustomer(NEW_CUSTOMER);
        performPutAndExpectStatusOk("/account", bankAccount)
                .andExpect(jsonPath("$.id", is(bankAccount.getId())))
                .andExpect(jsonPath("$.customer", is(NEW_CUSTOMER)));
    }

    @Test
    void remove() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer(NEW_CUSTOMER);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        performDeleteAndExpectStatusOk("/account/" + createdBankAccount.getId())
               .andExpect(jsonPath("$.id", is(createdBankAccount.getId())));

    }

}