package com.epam.brest.impl;

import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.CreditCardDtoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardDtoServiceImplIT extends BasicServiceIT {

    private final CreditCardDtoService creditCardDtoService;

    public CreditCardDtoServiceImplIT(@Autowired CreditCardDtoService creditCardDtoService) {
        this.creditCardDtoService = creditCardDtoService;
    }

    @Test
    void getAllWithAccountNumber() {
        List<CreditCardDto> cards = creditCardDtoService.getAllWithAccountNumber();
        assertNotNull(cards);
        assertTrue(cards.size() > 0);
    }

}