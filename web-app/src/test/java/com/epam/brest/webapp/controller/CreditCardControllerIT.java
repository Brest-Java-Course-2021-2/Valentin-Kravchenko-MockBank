package com.epam.brest.webapp.controller;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.api.CreditCardService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CreditCardControllerIT extends ControllerTestConfiguration {

    private final MockMvc mockMvc;
    private final CreditCardService creditCardService;

    public CreditCardControllerIT(@Autowired MockMvc mockMvc,
                                  @Autowired CreditCardService creditCardService) {
        this.mockMvc = mockMvc;
        this.creditCardService = creditCardService;
    }

    @Test
    void create() throws Exception {
        List<CreditCard> cardsBeforeCreate = creditCardService.getAllByAccountId(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("accountId", "1");
        mockMvc.perform(post("/card").params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accounts"))
                .andExpect(redirectedUrl("/accounts"))
                .andExpect(flash().attributeExists("message"));
        List<CreditCard> cardsAfterCreate = creditCardService.getAllByAccountId(1);
        assertEquals(cardsBeforeCreate.size(), cardsAfterCreate.size() - 1);
    }

    @Test
    void removeSucceeded() throws Exception {
        CreditCard createdCreditCard = creditCardService.create(1);
        String urlTemplate = "/card/" + createdCreditCard.getId() + "/remove";
        mockMvc.perform(post(urlTemplate))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/cards"))
               .andExpect(redirectedUrl("/cards"))
               .andExpect(flash().attributeExists("message"));
        assertThrows(CreditCardException.class, () -> creditCardService.delete(createdCreditCard.getId()));
    }

    @Test
    void removeFailed() throws Exception {
        List<CreditCard> cards = creditCardService.getAllByAccountId(1);
        CreditCard creditCard = cards.get(0);
        String urlTemplate = "/card/" + creditCard.getId() + "/remove";
        mockMvc.perform(post(urlTemplate))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/cards"))
               .andExpect(redirectedUrl("/cards"))
               .andExpect(flash().attributeExists("error"));
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCard, creditCardFromDb);
    }

    @Test
    void depositGET() throws Exception {
        mockMvc.perform(get("/card/1/deposit"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("transaction"))
                .andExpect(model().attribute("card", hasProperty("targetCardNumber", notNullValue())));
    }

    @Test
    void depositPOSTSucceeded() throws Exception {
        CreditCard creditCardFromDb = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targetCardNumber", creditCardFromDb.getNumber());
        params.add("valueSumOfMoney", "1000,55");
        params.add("locale", "ru");
        mockMvc.perform(post("/card/1/deposit").params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cards"))
                .andExpect(redirectedUrl("/cards"))
                .andExpect(flash().attributeExists("message"));
        BigDecimal updatedBalance = creditCardFromDb.getBalance().add(new BigDecimal("1000.55"));
        creditCardFromDb = creditCardService.getById(1);
        assertEquals(creditCardFromDb.getBalance(), updatedBalance);
    }

    @Test
    void depositPOSTFailed() throws Exception {
        // Case 1
        CreditCard creditCardFromDb = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("targetCardNumber", creditCardFromDb.getNumber());
        params.add("valueSumOfMoney", "1000,555");
        params.add("locale", "ru");
        mockMvc.perform(post("/card/1/deposit").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("transaction"))
                .andExpect(model().attribute("card", hasProperty("valueSumOfMoney", is("1000,555"))));
        // Case 2
        params.clear();
        params.add("targetCardNumber", creditCardFromDb.getNumber());
        params.add("valueSumOfMoney", "1000.55");
        params.add("locale", "ru");
        mockMvc.perform(post("/card/1/deposit").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("transaction"))
                .andExpect(model().attribute("card", hasProperty("valueSumOfMoney", is("1000.55"))));
        // Case 3
        params.clear();
        params.add("targetCardNumber", creditCardFromDb.getNumber());
        params.add("valueSumOfMoney", "0100,23");
        params.add("locale", "ru");
        mockMvc.perform(post("/card/1/deposit").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("transaction"))
                .andExpect(model().attribute("card", hasProperty("valueSumOfMoney", is("0100,23"))));
        // Case 4
        params.clear();
        params.add("targetCardNumber", creditCardFromDb.getNumber());
        params.add("valueSumOfMoney", "-1000");
        params.add("locale", "ru");
        mockMvc.perform(post("/card/1/deposit").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("transaction"))
                .andExpect(model().attribute("card", hasProperty("valueSumOfMoney", is("-1000"))));

    }

    @Test
    void transferGET() throws Exception {
        mockMvc.perform(get("/card/1/transfer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("transaction"))
                .andExpect(model().attribute("card", hasProperty("sourceCardNumber", notNullValue())));
    }

    @Test
    void transferPOSTSucceeded() throws Exception {
        CreditCard creditCardFromDbSource = creditCardService.getById(1);
        CreditCard creditCardFromDbTarget = creditCardService.getById(2);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("sourceCardNumber", creditCardFromDbSource.getNumber());
        params.add("targetCardNumber", creditCardFromDbTarget.getNumber());
        params.add("valueSumOfMoney", "100,1");
        params.add("locale", "ru");
        mockMvc.perform(post("/card/1/transfer").params(params))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cards"))
                .andExpect(redirectedUrl("/cards"))
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
    void transferPOSTFailed() throws Exception {
        // Case 1
        CreditCard creditCardFromDbSource = creditCardService.getById(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("sourceCardNumber", creditCardFromDbSource.getNumber());
        params.add("targetCardNumber", creditCardFromDbSource.getNumber());
        params.add("valueSumOfMoney", "100,1");
        params.add("locale", "ru");
        mockMvc.perform(post("/card/1/transfer").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("transaction"))
                .andExpect(model().attribute("card", hasProperty("targetCardNumber", is(creditCardFromDbSource.getNumber()))));
        // Case 2
        params.clear();
        String invalidCardNumber = "4929554996657100";
        params.add("sourceCardNumber", creditCardFromDbSource.getNumber());
        params.add("targetCardNumber", invalidCardNumber);
        params.add("valueSumOfMoney", "100,1");
        params.add("locale", "ru");
        mockMvc.perform(post("/card/1/transfer").params(params))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("transaction"))
                .andExpect(model().attribute("card", hasProperty("targetCardNumber", is(invalidCardNumber))));

    }

}