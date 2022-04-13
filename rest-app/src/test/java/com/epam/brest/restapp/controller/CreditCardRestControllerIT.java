package com.epam.brest.restapp.controller;

import com.epam.brest.model.CreditCard;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.api.CreditCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditCardRestControllerIT extends RestControllerTestBasic {

    public static final String TARGET_CARD_NUMBER = "targetCardNumber";
    public static final String SOURCE_CARD_NUMBER = "sourceCardNumber";
    public static final String TRANSACTION_AMOUNT_VALUE = "transactionAmountValue";
    public static final String LOCALE = "locale";
    public static final String RU = "ru";
    public static final String EN = "en";
    public static final String TRANSACTION_AMOUNT_RU = "1375,55";
    public static final String TRANSACTION_AMOUNT_EN = "1375.55";
    public static final String TRANSACTION_AMOUNT_IS_INCORRECT = "Transaction amount is incorrect!";
    public static final String TARGET_AND_SOURCE_CREDIT_CARD_NUMBERS_MUST_BE_DIFFERENT = "Target and source credit card numbers must be different!";
    public static final String SOURCE_CREDIT_CARD_NUMBER_IS_INVALID = "Source credit card number is invalid!";
    public static final String CARD_GET_ENDPOINT = "/card/%d";
    public static final String CARD_POST_ENDPOINT = "/card";
    public static final String CARD_GET_ENDPOINT_DEPOSIT = "/card/%d/deposit";
    public static final String CARD_POST_ENDPOINT_DEPOSIT = "/card/deposit";
    public static final String CARD_GET_ENDPOINT_TRANSFER = "/card/1/transfer";
    public static final String CARD_POST_ENDPOINT_TRANSFER = "/card/transfer";

    private final CreditCardService creditCardService;
    private final BankAccountService bankAccountService;

    public CreditCardRestControllerIT(MockMvc mockMvc,
                                      ObjectMapper objectMapper,
                                      CreditCardService creditCardServiceImpl,
                                      BankAccountService bankAccountServiceImpl) {
        super(mockMvc, objectMapper);
        this.creditCardService = creditCardServiceImpl;
        this.bankAccountService = bankAccountServiceImpl;
    }

    @Test
    void getById() throws Exception {
        performGetAndExpectStatusOk(format(CARD_GET_ENDPOINT, 1))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void create() throws Exception {
        List<CreditCard> cardsBeforeCreate = bankAccountService.getAllCardsById(1);
        performPostAndExpectStatusOk(CARD_POST_ENDPOINT, 1);
        List<CreditCard> cardsAfterCreate = bankAccountService.getAllCardsById(1);
        assertEquals(cardsBeforeCreate.size(), cardsAfterCreate.size() - 1);
    }

    @Test
    void delete() throws Exception {
        CreditCard createdCreditCard = creditCardService.create(1);
        performDeleteAndExpectStatusOk(format(CARD_GET_ENDPOINT, createdCreditCard.getId()))
               .andExpect(jsonPath("$.id", is(createdCreditCard.getId())));
    }

    @Test
    void deleteFail() throws Exception {
        List<CreditCard> cards = bankAccountService.getAllCardsById(1);
        CreditCard creditCard = cards.get(0);
        performDeleteAndExpectStatus(format(CARD_GET_ENDPOINT, creditCard.getId()), status().isBadRequest())
               .andExpect(jsonPath("$.errorMessage").hasJsonPath());
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCard, creditCardFromDb);
    }

    @Test
    void depositGET() throws Exception {
        CreditCard targetCreditCard = creditCardService.getById(1);
        performGetAndExpectStatusOk(format(CARD_GET_ENDPOINT_DEPOSIT, 1))
                .andExpect(jsonPath("$.targetCardNumber").value(targetCreditCard.getNumber()));
    }

    @Test
    void depositPOST() throws Exception {
        CreditCard targetCreditCard = creditCardService.create(1);
        Map<String, Object> body = new HashMap<>();
        body.put(TARGET_CARD_NUMBER, targetCreditCard.getNumber());
        body.put(TRANSACTION_AMOUNT_VALUE, TRANSACTION_AMOUNT_RU);
        body.put(LOCALE, RU);
        performPostAndExpectStatusOk(CARD_POST_ENDPOINT_DEPOSIT, body)
               .andExpect(jsonPath("$.balance").value(TRANSACTION_AMOUNT_EN));
    }

    @Test
    void depositPOSTWithIncorrectTransactionAmount() throws Exception {
        // Case 1
        CreditCard creditCardFromDb = creditCardService.getById(1);
        Map<String, Object> body = new HashMap<>();
        body.put(TARGET_CARD_NUMBER, creditCardFromDb.getNumber());
        body.put(TRANSACTION_AMOUNT_VALUE, TRANSACTION_AMOUNT_RU + "55");
        body.put(LOCALE, RU);
        performPostAndExpectStatus(CARD_POST_ENDPOINT_DEPOSIT, body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.transactionAmountValue").value(TRANSACTION_AMOUNT_IS_INCORRECT));
        // Case 2
        body.put(TRANSACTION_AMOUNT_VALUE, TRANSACTION_AMOUNT_EN);
        performPostAndExpectStatus(CARD_POST_ENDPOINT_DEPOSIT, body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.transactionAmountValue").value(TRANSACTION_AMOUNT_IS_INCORRECT));
        // Case 3
        body.put(TRANSACTION_AMOUNT_VALUE, "0" + TRANSACTION_AMOUNT_RU);
        performPostAndExpectStatus(CARD_POST_ENDPOINT_DEPOSIT, body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.transactionAmountValue").value(TRANSACTION_AMOUNT_IS_INCORRECT));
        // Case 4
        body.put(TRANSACTION_AMOUNT_VALUE, "%" + TRANSACTION_AMOUNT_RU);
        performPostAndExpectStatus(CARD_POST_ENDPOINT_DEPOSIT, body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.transactionAmountValue").value(TRANSACTION_AMOUNT_IS_INCORRECT));
    }

    @Test
    void transferGET() throws Exception {
        performGetAndExpectStatusOk(CARD_GET_ENDPOINT_TRANSFER)
                .andExpect(jsonPath("$.sourceCardNumber", notNullValue()));
    }

    @Test
    void transferPOST() throws Exception {
        CreditCard sourceCreditCard = creditCardService.getById(1);
        CreditCard targetCredit = creditCardService.create(1);
        Map<String, Object> body = new HashMap<>();
        body.put(SOURCE_CARD_NUMBER, sourceCreditCard.getNumber());
        body.put(TARGET_CARD_NUMBER, targetCredit.getNumber());
        body.put(TRANSACTION_AMOUNT_VALUE, sourceCreditCard.getBalance().toString());
        body.put(LOCALE, EN);
        performPostAndExpectStatusOk(CARD_POST_ENDPOINT_TRANSFER, body)
                .andExpect(jsonPath("$.balance").value("0.00"));
        CreditCard targetCreditFromDb = creditCardService.getById(targetCredit.getId());
        assertEquals(targetCreditFromDb.getBalance(), sourceCreditCard.getBalance());
    }

    @Test
    void transferPOSTWithInvalidCardNumbers() throws Exception {
        // Case 1
        CreditCard creditCardFromDb = creditCardService.getById(1);
        Map<String, Object> body = new HashMap<>();
        body.put(SOURCE_CARD_NUMBER, creditCardFromDb.getNumber());
        body.put(TARGET_CARD_NUMBER, creditCardFromDb.getNumber());
        body.put(TRANSACTION_AMOUNT_VALUE, TRANSACTION_AMOUNT_RU);
        body.put(LOCALE, RU);
        performPostAndExpectStatus(CARD_POST_ENDPOINT_TRANSFER, body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.targetCardNumber").value(TARGET_AND_SOURCE_CREDIT_CARD_NUMBERS_MUST_BE_DIFFERENT));
        // Case 2
        String invalidCardNumber = "4929554996657100";
        body.put(SOURCE_CARD_NUMBER, invalidCardNumber);
        body.put(TARGET_CARD_NUMBER, creditCardFromDb.getNumber());
        performPostAndExpectStatus(CARD_POST_ENDPOINT_TRANSFER, body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.sourceCardNumber").value(SOURCE_CREDIT_CARD_NUMBER_IS_INVALID));

    }

}