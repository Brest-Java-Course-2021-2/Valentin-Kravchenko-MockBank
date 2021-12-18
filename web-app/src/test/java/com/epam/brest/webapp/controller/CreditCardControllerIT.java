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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class CreditCardControllerIT extends ControllerTestBasic {

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
        params.add("accountId", "1");
        performPostAndExpectStatus3xxRedirection("/card", params, "redirect:/accounts","/accounts")
                .andExpect(flash().attributeExists("message"));
        List<CreditCard> cardsAfterCreate = creditCardService.getAllByAccountId(1);
        assertEquals(cardsBeforeCreate.size(), cardsAfterCreate.size() - 1);
    }

    @Test
    void remove() throws Exception {
        CreditCard createdCreditCard = creditCardService.create(1);
        String endpoint = "/card/" + createdCreditCard.getId() + "/remove";
        performPostAndExpectStatus3xxRedirection(endpoint, new LinkedMultiValueMap<>(), "redirect:/cards","/cards")
               .andExpect(flash().attributeExists("message"));
        assertThrows(CreditCardException.class, () -> creditCardService.delete(createdCreditCard.getId()));
    }

    @Test
    void failedRemove() throws Exception {
        List<CreditCard> cards = creditCardService.getAllByAccountId(1);
        CreditCard creditCard = cards.get(0);
        String endpoint = "/card/" + creditCard.getId() + "/remove";
        performPostAndExpectStatus3xxRedirection(endpoint, new LinkedMultiValueMap<>(), "redirect:/cards","/cards")
               .andExpect(flash().attributeExists("error"));
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCard, creditCardFromDb);
    }

    @Test
    void depositGET() throws Exception {
        performGetAndExpectStatusOk("/card/1/deposit", "transaction")
                .andExpect(model().attribute("card", hasProperty("targetCardNumber", notNullValue())));
    }

    @Test
    void depositPOST() throws Exception {
        CreditCard creditCardFromDb = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targetCardNumber", creditCardFromDb.getNumber());
        params.add("valueSumOfMoney", "1000,55");
        params.add("locale", "ru");
        performPostAndExpectStatus3xxRedirection("/card/1/deposit", params, "redirect:/cards","/cards")
                .andExpect(flash().attributeExists("message"));
        BigDecimal updatedBalance = creditCardFromDb.getBalance().add(new BigDecimal("1000.55"));
        creditCardFromDb = creditCardService.getById(1);
        assertEquals(creditCardFromDb.getBalance(), updatedBalance);
    }

    @Test
    void depositPOSTWithIncorrectSumOfMoney() throws Exception {
        // Case 1
        CreditCard creditCardFromDb = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targetCardNumber", creditCardFromDb.getNumber());
        params.add("valueSumOfMoney", "1000,555");
        params.add("locale", "ru");
        performPostAndExpectStatusOk("/card/1/deposit", params, "transaction")
                .andExpect(model().attribute("card", hasProperty("valueSumOfMoney", is("1000,555"))));
        // Case 2
        params.replace("valueSumOfMoney", List.of("1000.55"));
        performPostAndExpectStatusOk("/card/1/deposit", params, "transaction")
                .andExpect(model().attribute("card", hasProperty("valueSumOfMoney", is("1000.55"))));
        // Case 3
        params.replace("valueSumOfMoney", List.of("0100,23"));
        performPostAndExpectStatusOk("/card/1/deposit", params, "transaction")
                .andExpect(model().attribute("card", hasProperty("valueSumOfMoney", is("0100,23"))));
        // Case 4
        params.replace("valueSumOfMoney", List.of("-1000"));
        performPostAndExpectStatusOk("/card/1/deposit", params, "transaction")
                .andExpect(model().attribute("card", hasProperty("valueSumOfMoney", is("-1000"))));

    }

    @Test
    void transferGET() throws Exception {
        performGetAndExpectStatusOk("/card/1/transfer", "transaction")
                .andExpect(model().attribute("card", hasProperty("sourceCardNumber", notNullValue())));
    }

    @Test
    void transferPOST() throws Exception {
        CreditCard creditCardFromDbSource = creditCardService.getById(1);
        CreditCard creditCardFromDbTarget = creditCardService.getById(2);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("sourceCardNumber", creditCardFromDbSource.getNumber());
        params.add("targetCardNumber", creditCardFromDbTarget.getNumber());
        params.add("valueSumOfMoney", "100,1");
        params.add("locale", "ru");
        performPostAndExpectStatus3xxRedirection("/card/1/transfer", params, "redirect:/cards","/cards")
                .andExpect(flash().attributeExists("message"));
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
        params.add("sourceCardNumber", creditCardFromDbSource.getNumber());
        params.add("targetCardNumber", creditCardFromDbSource.getNumber());
        params.add("valueSumOfMoney", "100,1");
        params.add("locale", "ru");
        performPostAndExpectStatusOk("/card/1/transfer", params, "transaction")
                .andExpect(model().attribute("card", hasProperty("targetCardNumber", is(creditCardFromDbSource.getNumber()))));
        // Case 2
        String invalidCardNumber = "4929554996657100";
        params.replace("sourceCardNumber", List.of(creditCardFromDbSource.getNumber()));
        params.replace("targetCardNumber",  List.of(invalidCardNumber));
        performPostAndExpectStatusOk("/card/1/transfer", params, "transaction")
                .andExpect(model().attribute("card", hasProperty("targetCardNumber", is(invalidCardNumber))));

    }

}