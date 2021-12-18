package com.epam.brest.webapp.controller;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.api.ExtendedCreditCardService;
import com.epam.brest.service.exception.CreditCardException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.List;

import static com.epam.brest.webapp.constant.ControllerConstant.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class CreditCardControllerIT extends ControllerTestBasic {

    public static final String CARD_1_DEPOSIT = "/card/1/deposit";
    public static final String CARD_1_TRANSFER = "/card/1/transfer";
    public static final String ACCOUNT_ID = "accountId";
    public static final String TARGET_CARD_NUMBER = "targetCardNumber";
    public static final String SOURCE_CARD_NUMBER = "sourceCardNumber";
    public static final String VALUE_SUM_OF_MONEY = "valueSumOfMoney";
    public static final String LOCALE = "locale";

    private final ExtendedCreditCardService creditCardService;

    public CreditCardControllerIT(@Autowired MockMvc mockMvc,
                                  @Autowired ExtendedCreditCardService creditCardService) {
        super(mockMvc);
        this.creditCardService = creditCardService;
    }

    @Test
    void create() throws Exception {
        List<CreditCard> cardsBeforeCreate = creditCardService.getAllByAccountId(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(ACCOUNT_ID, "1");
        performPostAndExpectStatus3xxRedirection("/card", params, REDIRECT_ACCOUNTS, ACCOUNTS_ENDPOINT)
                .andExpect(flash().attributeExists(MESSAGE));
        List<CreditCard> cardsAfterCreate = creditCardService.getAllByAccountId(1);
        assertEquals(cardsBeforeCreate.size(), cardsAfterCreate.size() - 1);
    }

    @Test
    void remove() throws Exception {
        CreditCard createdCreditCard = creditCardService.create(1);
        String endpoint = "/card/" + createdCreditCard.getId() + "/remove";
        performPostAndExpectStatus3xxRedirection(endpoint, new LinkedMultiValueMap<>(), REDIRECT_CARDS, CARDS_ENDPOINT)
               .andExpect(flash().attributeExists(MESSAGE));
        assertThrows(CreditCardException.class, () -> creditCardService.delete(createdCreditCard.getId()));
    }

    @Test
    void failedRemove() throws Exception {
        List<CreditCard> cards = creditCardService.getAllByAccountId(1);
        CreditCard creditCard = cards.get(0);
        String endpoint = "/card/" + creditCard.getId() + "/remove";
        performPostAndExpectStatus3xxRedirection(endpoint, new LinkedMultiValueMap<>(), REDIRECT_CARDS, CARDS_ENDPOINT)
               .andExpect(flash().attributeExists(ERROR));
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCard, creditCardFromDb);
    }

    @Test
    void depositGET() throws Exception {
        performGetAndExpectStatusOk(CARD_1_DEPOSIT, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(TARGET_CARD_NUMBER, notNullValue())));
    }

    @Test
    void depositPOST() throws Exception {
        CreditCard creditCardFromDb = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(TARGET_CARD_NUMBER, creditCardFromDb.getNumber());
        params.add(VALUE_SUM_OF_MONEY, "1000,55");
        params.add(LOCALE, "ru");
        performPostAndExpectStatus3xxRedirection(CARD_1_DEPOSIT, params, REDIRECT_CARDS, CARDS_ENDPOINT)
                .andExpect(flash().attributeExists(MESSAGE));
        BigDecimal updatedBalance = creditCardFromDb.getBalance().add(new BigDecimal("1000.55"));
        creditCardFromDb = creditCardService.getById(1);
        assertEquals(creditCardFromDb.getBalance(), updatedBalance);
    }

    @Test
    void depositPOSTWithIncorrectSumOfMoney() throws Exception {
        // Case 1
        CreditCard creditCardFromDb = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(TARGET_CARD_NUMBER, creditCardFromDb.getNumber());
        params.add(VALUE_SUM_OF_MONEY, "1000,555");
        params.add(LOCALE, "ru");
        performPostAndExpectStatusOk(CARD_1_DEPOSIT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(VALUE_SUM_OF_MONEY, is("1000,555"))));
        // Case 2
        params.replace(VALUE_SUM_OF_MONEY, List.of("1000.55"));
        performPostAndExpectStatusOk(CARD_1_DEPOSIT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(VALUE_SUM_OF_MONEY, is("1000.55"))));
        // Case 3
        params.replace(VALUE_SUM_OF_MONEY, List.of("0100,23"));
        performPostAndExpectStatusOk(CARD_1_DEPOSIT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(VALUE_SUM_OF_MONEY, is("0100,23"))));
        // Case 4
        params.replace(VALUE_SUM_OF_MONEY, List.of("-1000"));
        performPostAndExpectStatusOk(CARD_1_DEPOSIT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(VALUE_SUM_OF_MONEY, is("-1000"))));
    }

    @Test
    void transferGET() throws Exception {
        performGetAndExpectStatusOk(CARD_1_TRANSFER, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(SOURCE_CARD_NUMBER, notNullValue())));
    }

    @Test
    void transferPOST() throws Exception {
        CreditCard creditCardFromDbSource = creditCardService.getById(1);
        CreditCard creditCardFromDbTarget = creditCardService.getById(2);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(SOURCE_CARD_NUMBER, creditCardFromDbSource.getNumber());
        params.add(TARGET_CARD_NUMBER, creditCardFromDbTarget.getNumber());
        params.add(VALUE_SUM_OF_MONEY, "100,1");
        params.add(LOCALE, "ru");
        performPostAndExpectStatus3xxRedirection(CARD_1_TRANSFER, params, REDIRECT_CARDS, CARDS_ENDPOINT)
                .andExpect(flash().attributeExists(MESSAGE));
        BigDecimal sumOfMoney = new BigDecimal("100.1");
        BigDecimal sourceBalance = creditCardFromDbSource.getBalance().subtract(sumOfMoney);
        BigDecimal targetBalance = creditCardFromDbTarget.getBalance().add(sumOfMoney);
        creditCardFromDbSource = creditCardService.getById(1);
        creditCardFromDbTarget = creditCardService.getById(2);
        assertEquals(creditCardFromDbSource.getBalance(), sourceBalance);
        assertEquals(creditCardFromDbTarget.getBalance(), targetBalance);
    }

    @Test
    void transferPOSTWithInvalidCardNumbers() throws Exception {
        // Case 1
        CreditCard creditCardFromDbSource = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(SOURCE_CARD_NUMBER, creditCardFromDbSource.getNumber());
        params.add(TARGET_CARD_NUMBER, creditCardFromDbSource.getNumber());
        params.add(VALUE_SUM_OF_MONEY, "100,1");
        params.add(LOCALE, "ru");
        performPostAndExpectStatusOk(CARD_1_TRANSFER, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(TARGET_CARD_NUMBER, is(creditCardFromDbSource.getNumber()))));
        // Case 2
        String invalidCardNumber = "4929554996657100";
        params.replace(SOURCE_CARD_NUMBER, List.of(creditCardFromDbSource.getNumber()));
        params.replace(TARGET_CARD_NUMBER,  List.of(invalidCardNumber));
        performPostAndExpectStatusOk(CARD_1_TRANSFER, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(TARGET_CARD_NUMBER, is(invalidCardNumber))));
    }

}