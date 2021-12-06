package com.epam.brest.service.impl;

import com.epam.brest.model.dto.CreditCardDateRangeDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.service.CreditCardDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreditCardDtoServiceImplIT extends BasicServiceIT {

    private final CreditCardDtoService creditCardDtoService;

    private List<CreditCardDto> cards;
    private CreditCardDto firstCreditCard;
    private CreditCardDto lastCreditCard;

    public CreditCardDtoServiceImplIT(@Autowired CreditCardDtoService creditCardDtoService) {
        this.creditCardDtoService = creditCardDtoService;
    }

    @BeforeEach
    void setup(){
        cards = creditCardDtoService.getAllWithAccountNumber();
        firstCreditCard = cards.get(0);
        lastCreditCard = cards.get(cards.size() - 1);
    }

    @Test
    void getAllWithAccountNumber() {
        assertNotNull(cards);
        assertTrue(cards.size() > 0);
        assertNotNull(firstCreditCard);
        assertNotNull(lastCreditCard);

    }

    @Test
    void getAllWithAccountNumberByFilter() {
        //Case 1
        CreditCardDateRangeDto creditCardDateRangeDto = new CreditCardDateRangeDto();
        creditCardDateRangeDto.setFromDate(firstCreditCard.getExpirationDate());
        creditCardDateRangeDto.setToDate(lastCreditCard.getExpirationDate());
        List<CreditCardDto> cardsByFilter = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        assertEquals(cards, cardsByFilter);
        //Case 1
        creditCardDateRangeDto.setFromDate(lastCreditCard.getExpirationDate().plusYears(10));
        creditCardDateRangeDto.setToDate(null);
        cardsByFilter = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        assertNotNull(cardsByFilter);
        assertEquals(cardsByFilter.size(), 0);
        //Case 2
        creditCardDateRangeDto.setFromDate(null);
        creditCardDateRangeDto.setToDate(firstCreditCard.getExpirationDate().minusYears(10));
        cardsByFilter = creditCardDtoService.getAllWithAccountNumber(creditCardDateRangeDto);
        assertNotNull(cardsByFilter);
        assertEquals(cardsByFilter.size(), 0);
    }

}