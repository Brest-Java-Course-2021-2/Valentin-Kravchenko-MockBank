package com.epam.brest.restapp.controller;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.api.ExtendedCreditCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreditCardControllerIT extends RestControllerTestBasic {

    public static final String TARGET_CARD_NUMBER = "targetCardNumber";
    public static final String SOURCE_CARD_NUMBER = "sourceCardNumber";
    public static final String VALUE_SUM_OF_MONEY = "valueSumOfMoney";
    public static final String LOCALE = "locale";
    public static final String SUM_OF_MONEY_IS_INCORRECT = "Sum of money is incorrect!";

    private final ExtendedCreditCardService creditCardService;

    public CreditCardControllerIT(@Autowired MockMvc mockMvc,
                                  @Autowired ObjectMapper objectMapper,
                                  @Autowired ExtendedCreditCardService creditCardService) {
        super(mockMvc, objectMapper);
        this.creditCardService = creditCardService;
    }

    @Test
    void getById() throws Exception {
        performGetAndExpectStatusOk("/card/1")
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void create() throws Exception {
        List<CreditCard> cardsBeforeCreate = creditCardService.getAllByAccountId(1);
        performPostAndExpectStatusOk("/card", 1);
        List<CreditCard> cardsAfterCreate = creditCardService.getAllByAccountId(1);
        assertEquals(cardsBeforeCreate.size(), cardsAfterCreate.size() - 1);
    }

    @Test
    void remove() throws Exception {
        CreditCard createdCreditCard = creditCardService.create(1);
        performDeleteAndExpectStatusOk("/card/" + createdCreditCard.getId())
               .andExpect(jsonPath("$.id", is(createdCreditCard.getId())));
    }

    @Test
    void removeFailed() throws Exception {
        List<CreditCard> cards = creditCardService.getAllByAccountId(1);
        CreditCard creditCard = cards.get(0);
        performDeleteAndExpectStatus("/card/" + creditCard.getId(), status().isBadRequest())
               .andExpect(jsonPath("$.message").hasJsonPath());
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCard, creditCardFromDb);
    }

    @Test
    void depositGET() throws Exception {
        performGetAndExpectStatusOk("/card/1/deposit").andExpect(jsonPath("$.targetCardNumber", notNullValue()));
    }

    @Test
    void depositPOST() throws Exception {
        CreditCard creditCardFromDb = creditCardService.getById(1);
        Map<String, Object> body = new HashMap<>();
        body.put(TARGET_CARD_NUMBER, creditCardFromDb.getNumber());
        body.put(VALUE_SUM_OF_MONEY, "1 000,55");
        body.put(LOCALE, "ru");
        BigDecimal balanceTargetCreditCardAfterDeposit = creditCardFromDb.getBalance().add(new BigDecimal("1000.55"));
        performPostAndExpectStatusOk("/card/1/deposit", body)
               .andExpect(jsonPath("$.balance").value(balanceTargetCreditCardAfterDeposit.toString()));
    }

    @Test
    void depositPOSTWithIncorrectSumOfMoney() throws Exception {
        // Case 1
        CreditCard creditCardFromDb = creditCardService.getById(1);
        Map<String, Object> body = new HashMap<>();
        body.put(TARGET_CARD_NUMBER, creditCardFromDb.getNumber());
        body.put(VALUE_SUM_OF_MONEY, "1000,555");
        body.put(LOCALE, "ru");
        performPostAndExpectStatus("/card/1/deposit", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueSumOfMoney").value(SUM_OF_MONEY_IS_INCORRECT));
        // Case 2
        body.put(VALUE_SUM_OF_MONEY, "1000.55");
        performPostAndExpectStatus("/card/1/deposit", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueSumOfMoney").value(SUM_OF_MONEY_IS_INCORRECT));
        // Case 3
        body.put(VALUE_SUM_OF_MONEY, "0100,23");
        performPostAndExpectStatus("/card/1/deposit", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueSumOfMoney").value(SUM_OF_MONEY_IS_INCORRECT));
        // Case 4
        body.put(VALUE_SUM_OF_MONEY, "-1000");
        performPostAndExpectStatus("/card/1/deposit", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueSumOfMoney").value(SUM_OF_MONEY_IS_INCORRECT));
    }

    @Test
    void transferGET() throws Exception {
        performGetAndExpectStatusOk("/card/1/transfer").andExpect(jsonPath("$.sourceCardNumber", notNullValue()));
    }

    @Test
    void transferPOST() throws Exception {
        CreditCard sourceCreditCardFromDb = creditCardService.getById(1);
        CreditCard targetCreditCardFromDb = creditCardService.getById(2);
        Map<String, Object> body = new HashMap<>();
        body.put(SOURCE_CARD_NUMBER, sourceCreditCardFromDb.getNumber());
        body.put(TARGET_CARD_NUMBER, targetCreditCardFromDb.getNumber());
        body.put(VALUE_SUM_OF_MONEY, "100,1");
        body.put(LOCALE, "ru");
        BigDecimal sumOfMoney = new BigDecimal("100.1");
        BigDecimal balanceSourceCreditCardAfterTransfer = sourceCreditCardFromDb.getBalance().subtract(sumOfMoney);
        performPostAndExpectStatusOk("/card/1/transfer", body)
                .andExpect(jsonPath("$.balance").value(balanceSourceCreditCardAfterTransfer.toString()));
        BigDecimal balanceTargetCreditCardAfterTransfer = targetCreditCardFromDb.getBalance().add(sumOfMoney);
        targetCreditCardFromDb = creditCardService.getById(2);
        assertEquals(targetCreditCardFromDb.getBalance(), balanceTargetCreditCardAfterTransfer);
    }

    @Test
    void transferPOSTWithInvalidCardNumbers() throws Exception {
        // Case 1
        CreditCard sourceCreditCardFromDb = creditCardService.getById(1);
        Map<String, Object> body = new HashMap<>();
        body.put(SOURCE_CARD_NUMBER, sourceCreditCardFromDb.getNumber());
        body.put(TARGET_CARD_NUMBER, sourceCreditCardFromDb.getNumber());
        body.put(VALUE_SUM_OF_MONEY, "100,1");
        body.put(LOCALE, "ru");
        performPostAndExpectStatus("/card/1/transfer", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.targetCardNumber").value("Target and source credit card numbers must be different!"));
        // Case 2
        String invalidCardNumber = "4929554996657100";
        body.put(SOURCE_CARD_NUMBER, sourceCreditCardFromDb.getNumber());
        body.put(TARGET_CARD_NUMBER, invalidCardNumber);
        performPostAndExpectStatus("/card/1/transfer", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.targetCardNumber").value("Target credit card number is invalid!"));

    }

}