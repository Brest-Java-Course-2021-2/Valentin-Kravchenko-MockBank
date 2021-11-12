package com.epam.brest.impl;

import com.epam.brest.BasicServiseTest;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardServiceImplIT extends BasicServiseTest {

    private final CreditCardService creditCardService;

    public CreditCardServiceImplIT(@Autowired CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Test
    void create() {
        Integer id = 1;
        CreditCard creditCard = creditCardService.create(id);
        assertNotNull(creditCard.getId());
        assertNotNull(creditCard.getNumber());
        assertEquals(creditCard.getBalance().intValue(), 0);
        assertEquals(creditCard.getAccountId(), id);
    }

    @Test
    void delete() {
        Integer id = 1;
        CreditCard creditCard = creditCardService.create(id);
        Integer result = creditCardService.delete(creditCard);
        assertEquals(result, 1);
    }
    
}