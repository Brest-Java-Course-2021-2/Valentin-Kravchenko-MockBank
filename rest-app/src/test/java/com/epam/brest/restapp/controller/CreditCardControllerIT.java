package com.epam.brest.restapp.controller;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.api.CreditCardService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CreditCardControllerIT extends RestControllerTestBasic {

    private final CreditCardService creditCardService;

    public CreditCardControllerIT(@Autowired MockMvc mockMvc,
                                  @Autowired ObjectMapper objectMapper,
                                  @Autowired CreditCardService creditCardService) {
        super(mockMvc, objectMapper);
        this.creditCardService = creditCardService;
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
        body.put("targetCardNumber", creditCardFromDb.getNumber());
        body.put("valueSumOfMoney", "1000,55");
        body.put("locale", "ru");
        performPostAndExpectStatusOk("/card/1/deposit", body)
               .andExpect(content().string("true"));
        BigDecimal updatedBalance = creditCardFromDb.getBalance().add(new BigDecimal("1000.55"));
        creditCardFromDb = creditCardService.getById(1);
        assertEquals(creditCardFromDb.getBalance(), updatedBalance);
    }

    @Test
    void depositPOSTWithIncorrectSumOfMoney() throws Exception {
        // Case 1
        CreditCard creditCardFromDb = creditCardService.getById(1);
        Map<String, Object> body = new HashMap<>();
        body.put("targetCardNumber", creditCardFromDb.getNumber());
        body.put("valueSumOfMoney", "1000,555");
        body.put("locale", "ru");
        performPostAndExpectStatus("/card/1/deposit", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueSumOfMoney").value("Sum of money is incorrect!"));
        // Case 2
        body.put("valueSumOfMoney", "1000.55");
        performPostAndExpectStatus("/card/1/deposit", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueSumOfMoney").value("Sum of money is incorrect!"));
        // Case 3
        body.put("valueSumOfMoney", "0100,23");
        performPostAndExpectStatus("/card/1/deposit", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueSumOfMoney").value("Sum of money is incorrect!"));
        // Case 4
        body.put("valueSumOfMoney", "-1000");
        performPostAndExpectStatus("/card/1/deposit", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.valueSumOfMoney").value("Sum of money is incorrect!"));

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
        body.put("sourceCardNumber", sourceCreditCardFromDb.getNumber());
        body.put("targetCardNumber", targetCreditCardFromDb.getNumber());
        body.put("valueSumOfMoney", "100,1");
        body.put("locale", "ru");
        performPostAndExpectStatusOk("/card/1/transfer", body).andExpect(content().string("true"));
        BigDecimal sumOfMoney = new BigDecimal("100.1");
        BigDecimal sourceBalance = sourceCreditCardFromDb.getBalance().subtract(sumOfMoney);
        BigDecimal targetBalance = targetCreditCardFromDb.getBalance().add(sumOfMoney);
        sourceCreditCardFromDb = creditCardService.getById(1);
        targetCreditCardFromDb = creditCardService.getById(2);
        assertEquals(sourceCreditCardFromDb.getBalance(), sourceBalance);
        assertEquals(targetCreditCardFromDb.getBalance(), targetBalance);
    }

    @Test
    void transferPOSTWithInvalidCardNumbers() throws Exception {
        // Case 1
        CreditCard sourceCreditCardFromDb = creditCardService.getById(1);
        Map<String, Object> body = new HashMap<>();
        body.put("sourceCardNumber", sourceCreditCardFromDb.getNumber());
        body.put("targetCardNumber", sourceCreditCardFromDb.getNumber());
        body.put("valueSumOfMoney", "100,1");
        body.put("locale", "ru");
        performPostAndExpectStatus("/card/1/transfer", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.targetCardNumber").value("Target and source credit card numbers must be different!"));
        // Case 2
        String invalidCardNumber = "4929554996657100";
        body.put("sourceCardNumber", sourceCreditCardFromDb.getNumber());
        body.put("targetCardNumber", invalidCardNumber);
        performPostAndExpectStatus("/card/1/transfer", body, status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.targetCardNumber").value("Target credit card number is invalid!"));

    }

}