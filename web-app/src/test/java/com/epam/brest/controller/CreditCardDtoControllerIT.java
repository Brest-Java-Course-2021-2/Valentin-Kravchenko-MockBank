package com.epam.brest.controller;

import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.CreditCardDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CreditCardDtoControllerIT extends BasicControllerTest {

    private final CreditCardDtoService creditCardDtoService;

    private List<CreditCardDto> cards;

    public CreditCardDtoControllerIT(@Autowired CreditCardDtoService creditCardDtoService) {
        this.creditCardDtoService = creditCardDtoService;
    }

    @BeforeEach
    void setup(){
        cards = creditCardDtoService.getAllWithAccountNumber();
    }

    @Test
    void cards() throws Exception {
        mockMvc.perform(get("/cards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("cards"))
                .andExpect(model().attribute("cards", cards));
    }
    
}