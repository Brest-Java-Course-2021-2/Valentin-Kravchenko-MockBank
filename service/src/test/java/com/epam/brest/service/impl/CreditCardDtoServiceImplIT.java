package com.epam.brest.service.impl;

import com.epam.brest.model.CreditCardDto;
import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.service.annotation.ServiceIT;
import com.epam.brest.service.api.CreditCardDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDate;
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
        creditCardFilterDto.setFromDateValue(convertToRangeDateFormat(firstCreditCard.getExpirationDate()));
        creditCardFilterDto.setToDateValue(convertToRangeDateFormat(lastCreditCard.getExpirationDate()));
        List<CreditCardDto> cardsByFilter = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        assertEquals(cards, cardsByFilter);
        //Case 2
        creditCardFilterDto.setFromDateValue(convertToRangeDateFormat(lastCreditCard.getExpirationDate().plusYears(10)));
        creditCardFilterDto.setToDateValue(null);
        cardsByFilter = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        assertNotNull(cardsByFilter);
        assertEquals(cardsByFilter.size(), 0);
        //Case 3
        creditCardFilterDto.setFromDateValue(null);
        creditCardFilterDto.setToDateValue(convertToRangeDateFormat(firstCreditCard.getExpirationDate().minusYears(10)));
        cardsByFilter = creditCardDtoService.getAllWithAccountNumber(creditCardFilterDto);
        assertNotNull(cardsByFilter);
        assertEquals(cardsByFilter.size(), 0);
    }

    private String convertToRangeDateFormat(LocalDate date) {
        int monthIntValue = date.getMonth().getValue();
        String monthIntStrValue = String.valueOf(monthIntValue);
        if (monthIntStrValue.length() == 1) {
            monthIntStrValue = "0" + monthIntStrValue;
        }
        return monthIntStrValue + "/" + date.getYear();
    }

}