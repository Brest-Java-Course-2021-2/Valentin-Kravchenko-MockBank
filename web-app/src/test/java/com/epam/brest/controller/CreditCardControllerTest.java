package com.epam.brest.controller;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class CreditCardControllerTest extends BasicControllerTest {

    private final CreditCardService creditCardService;

    public CreditCardControllerTest(@Autowired CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Test
    void create() throws Exception {
        List<CreditCard> cardsBeforeCreate = creditCardService.getAllByAccountId(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("accountId", "1");
        params.add("accountNumber", "Customer");
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
        CreditCard creditCard = creditCardService.create(1);
        String urlTemplate = "/card/" + creditCard.getId() + "/remove";
        mockMvc.perform(post(urlTemplate))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/cards"))
               .andExpect(redirectedUrl("/cards"))
               .andExpect(flash().attributeExists("message"));
        assertThrows(IllegalArgumentException.class, () -> creditCardService.delete(creditCard));
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

}