package com.epam.brest.webapp.controller;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import com.epam.brest.service.exception.CreditCardException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

}