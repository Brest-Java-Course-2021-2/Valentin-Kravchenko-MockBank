package com.epam.brest.webapp.controller;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.api.CreditCardService;
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

    public static final String CARD_TEST_ID_DEPOSIT_ENDPOINT = "/card/1/deposit";
    public static final String CARD_TEST_ID_TRANSFER_ENDPOINT = "/card/1/transfer";
    public static final String CARD_TEMPLATE_REMOVE_ENDPOINT = "/card/%d/remove";
    public static final String ACCOUNT_ID = "accountId";
    public static final String TARGET_CARD_NUMBER = "targetCardNumber";
    public static final String SOURCE_CARD_NUMBER = "sourceCardNumber";
    public static final String VALUE_SUM_OF_MONEY = "valueSumOfMoney";
    public static final String LOCALE = "locale";
    public static final String RU = "ru";

    private final CreditCardService creditCardService;
    private final BankAccountService bankAccountService;

    public CreditCardControllerIT(@Autowired MockMvc mockMvc,
                                  @Autowired CreditCardService creditCardServiceRest,
                                  @Autowired BankAccountService bankAccountServiceRest) {
        super(mockMvc);
        this.creditCardService = creditCardServiceRest;
        this.bankAccountService = bankAccountServiceRest;
    }

    @Test
    void create() throws Exception {
        List<CreditCard> cardsBeforeCreate = bankAccountService.getAllCardsById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(ACCOUNT_ID, "1");
        performPostAndExpectStatus3xxRedirection("/card", params, REDIRECT_ACCOUNTS, ACCOUNTS_ENDPOINT)
                .andExpect(flash().attributeExists(MESSAGE));
        List<CreditCard> cardsAfterCreate = bankAccountService.getAllCardsById(1);
        assertEquals(cardsBeforeCreate.size(), cardsAfterCreate.size() - 1);
    }

    @Test
    void remove() throws Exception {
        CreditCard createdCreditCard = creditCardService.create(1);
        String endpoint = String.format(CARD_TEMPLATE_REMOVE_ENDPOINT, createdCreditCard.getId());
        performPostAndExpectStatus3xxRedirection(endpoint, new LinkedMultiValueMap<>(), REDIRECT_CARDS, CARDS_ENDPOINT)
               .andExpect(flash().attributeExists(MESSAGE));
        assertThrows(CreditCardException.class, () -> creditCardService.delete(createdCreditCard.getId()));
    }

    @Test
    void failedRemove() throws Exception {
        List<CreditCard> cards = bankAccountService.getAllCardsById(1);
        CreditCard creditCard = cards.get(0);
        String endpoint = String.format(CARD_TEMPLATE_REMOVE_ENDPOINT, creditCard.getId());
        performPostAndExpectStatus3xxRedirection(endpoint, new LinkedMultiValueMap<>(), REDIRECT_CARDS, CARDS_ENDPOINT)
               .andExpect(flash().attributeExists(ERROR));
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCard, creditCardFromDb);
    }

    @Test
    void depositGET() throws Exception {
        performGetAndExpectStatusOk(CARD_TEST_ID_DEPOSIT_ENDPOINT, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(TARGET_CARD_NUMBER, notNullValue())));
    }

    @Test
    void depositPOST() throws Exception {
        CreditCard targetCreditCardFromDb = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(TARGET_CARD_NUMBER, targetCreditCardFromDb.getNumber());
        params.add(VALUE_SUM_OF_MONEY, "1000,55");
        params.add(LOCALE, RU);
        performPostAndExpectStatus3xxRedirection(CARD_TEST_ID_DEPOSIT_ENDPOINT, params, REDIRECT_CARDS, CARDS_ENDPOINT)
                .andExpect(flash().attributeExists(MESSAGE));
        BigDecimal updatedBalance = targetCreditCardFromDb.getBalance().add(new BigDecimal("1000.55"));
        targetCreditCardFromDb = creditCardService.getById(1);
        assertEquals(targetCreditCardFromDb.getBalance(), updatedBalance);
    }

    @Test
    void depositPOSTWithIncorrectSumOfMoney() throws Exception {
        // Case 1
        CreditCard creditCardFromDb = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(TARGET_CARD_NUMBER, creditCardFromDb.getNumber());
        params.add(VALUE_SUM_OF_MONEY, "1000,555");
        params.add(LOCALE, RU);
        performPostAndExpectStatusOk(CARD_TEST_ID_DEPOSIT_ENDPOINT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(VALUE_SUM_OF_MONEY, is("1000,555"))));
        // Case 2
        params.replace(VALUE_SUM_OF_MONEY, List.of("1000.55"));
        performPostAndExpectStatusOk(CARD_TEST_ID_DEPOSIT_ENDPOINT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(VALUE_SUM_OF_MONEY, is("1000.55"))));
        // Case 3
        params.replace(VALUE_SUM_OF_MONEY, List.of("0100,23"));
        performPostAndExpectStatusOk(CARD_TEST_ID_DEPOSIT_ENDPOINT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(VALUE_SUM_OF_MONEY, is("0100,23"))));
        // Case 4
        params.replace(VALUE_SUM_OF_MONEY, List.of("-1000"));
        performPostAndExpectStatusOk(CARD_TEST_ID_DEPOSIT_ENDPOINT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(VALUE_SUM_OF_MONEY, is("-1000"))));
    }

    @Test
    void transferGET() throws Exception {
        performGetAndExpectStatusOk(CARD_TEST_ID_TRANSFER_ENDPOINT, TRANSACTION)
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
        params.add(LOCALE, RU);
        performPostAndExpectStatus3xxRedirection(CARD_TEST_ID_TRANSFER_ENDPOINT, params, REDIRECT_CARDS, CARDS_ENDPOINT)
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
        params.add(LOCALE, RU);
        performPostAndExpectStatusOk(CARD_TEST_ID_TRANSFER_ENDPOINT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(TARGET_CARD_NUMBER, is(creditCardFromDbSource.getNumber()))));
        // Case 2
        String invalidCardNumber = "4929554996657100";
        params.replace(SOURCE_CARD_NUMBER, List.of(creditCardFromDbSource.getNumber()));
        params.replace(TARGET_CARD_NUMBER,  List.of(invalidCardNumber));
        performPostAndExpectStatusOk(CARD_TEST_ID_TRANSFER_ENDPOINT, params, TRANSACTION)
                .andExpect(model().attribute(CARD, hasProperty(TARGET_CARD_NUMBER, is(invalidCardNumber))));
    }

}