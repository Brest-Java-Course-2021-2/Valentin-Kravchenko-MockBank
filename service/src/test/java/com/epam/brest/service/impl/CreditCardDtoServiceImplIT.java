package com.epam.brest.service.impl;

import com.epam.brest.model.CreditCardDto;
import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.service.annotation.ServiceIT;
import com.epam.brest.service.api.CreditCardDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ServiceIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditCardDtoServiceImplIT {

    private final CreditCardDtoService creditCardDtoService;

    private List<CreditCardDto> cards;
    private CreditCardDto firstCreditCard;
    private CreditCardDto lastCreditCard;

    public CreditCardDtoServiceImplIT(CreditCardDtoService creditCardDtoService) {
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
        assertTrue(cards.size() > 0);
        assertNotNull(firstCreditCard);
        assertNotNull(lastCreditCard);
    }

    @Test
    void getAllWithAccountNumberByFilter() {
        //Case 1
        CreditCardFilterDto creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDate(firstCreditCard.getExpirationDate());
        creditCardFilterDto.setToDate(lastCreditCard.getExpirationDate());
        List<CreditCardDto> cardsByFilter = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        assertEquals(cards, cardsByFilter);
        //Case 2
        creditCardFilterDto.setFromDate(lastCreditCard.getExpirationDate().plusYears(10));
        creditCardFilterDto.setToDate(null);
        cardsByFilter = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        assertNotNull(cardsByFilter);
        assertEquals(cardsByFilter.size(), 0);
        //Case 3
        creditCardFilterDto.setFromDate(null);
        creditCardFilterDto.setToDate(firstCreditCard.getExpirationDate().minusYears(10));
        cardsByFilter = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        assertNotNull(cardsByFilter);
        assertEquals(cardsByFilter.size(), 0);
    }

}