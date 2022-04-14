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

import static java.lang.String.format;
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
    public static final String ACCOUNT_ENDPOINT = "/account";
    public static final String ACCOUNT_ID_ENDPOINT = "/account/%d";
    public static final String ACCOUNT_ID_ENDPOINT_CARDS = "/account/%d/cards";

    private final BankAccountService bankAccountService;

    public BankAccountRestControllerIT(MockMvc mockMvc,
                                       ObjectMapper objectMapper,
                                       BankAccountService bankAccountService) {
        super(mockMvc, objectMapper);
        this.bankAccountService = bankAccountService;
    }

    @Test
    void get() throws Exception {
        performGetAndExpectStatusOk(ACCOUNT_ENDPOINT)
                .andExpect(jsonPath("$.registrationDate", is(LocalDate.now().toString())));
    }

    @Test
    void getById() throws Exception {
        BankAccount bankAccount = bankAccountService.getById(1);
        performGetAndExpectStatusOk(format(ACCOUNT_ID_ENDPOINT, 1))
                .andExpect(jsonPath("$.id", is(bankAccount.getId())))
                .andExpect(jsonPath("$.customer", is(bankAccount.getCustomer())));
    }

    @Test
    void getAllCardsById() throws Exception {
        List<CreditCard> cards = bankAccountService.getAllCardsById(1);
        int lastIdx = cards.size() - 1;
        performGetAndExpectStatusOk(format(ACCOUNT_ID_ENDPOINT_CARDS, 1))
                .andExpect(jsonPath("$.size()", is(cards.size())))
                .andExpect(jsonPath("$[0].accountId", is(1)))
                .andExpect(jsonPath("$[" + lastIdx + "].accountId", is(1)));
    }

    @Test
    void getByNonExistingId() throws Exception {
        performGetAndExpectStatus(format(ACCOUNT_ID_ENDPOINT, 100), status().isNotFound())
               .andExpect(jsonPath("$.errorMessage").value(containsString("100")));
    }

    @Test
    void getByIncorrectId() throws Exception {
        performGetAndExpectStatus(ACCOUNT_ENDPOINT + "/one", status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(containsString("one")));
    }

    @Test
    void getByIdInvalidEndpoint() throws Exception {
        performGetAndExpectStatus(ACCOUNT_ENDPOINT + "/1/1", status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value(RESOURCE_NOT_FOUND));
    }

    @Test
    void create() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put(CUSTOMER, NEW_CUSTOMER);
        performPostAndExpectStatusOk(ACCOUNT_ENDPOINT, body)
               .andExpect(jsonPath("$.customer", is(NEW_CUSTOMER)));
    }

    @Test
    void createFail() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put(CUSTOMER, NEW_CUSTOMER + "1");
        performPostAndExpectStatus(ACCOUNT_ENDPOINT, body, status().isBadRequest())
               .andExpect(jsonPath("$.validationErrors.customer").value(CUSTOMER_FULL_NAME_IS_INCORRECT));

    }

    @Test
    void update() throws Exception {
        BankAccount bankAccount = bankAccountService.getById(1);
        bankAccount.setCustomer(NEW_CUSTOMER);
        performPutAndExpectStatusOk(ACCOUNT_ENDPOINT, bankAccount)
                .andExpect(jsonPath("$.id", is(bankAccount.getId())))
                .andExpect(jsonPath("$.customer", is(NEW_CUSTOMER)));
    }

    @Test
    void delete() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer(NEW_CUSTOMER);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        performDeleteAndExpectStatusOk(format(ACCOUNT_ID_ENDPOINT, createdBankAccount.getId()))
               .andExpect(jsonPath("$.id", is(createdBankAccount.getId())));

    }

}