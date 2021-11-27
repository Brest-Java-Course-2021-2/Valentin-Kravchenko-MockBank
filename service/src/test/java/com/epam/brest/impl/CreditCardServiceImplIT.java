package com.epam.brest.impl;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardServiceImplIT extends BasicServiceIT {

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
    void deleteSucceeded() {
        Integer id = 1;
        CreditCard creditCard = creditCardService.create(id);
        Integer result = creditCardService.delete(creditCard);
        assertEquals(result, 1);
    }

    @Test
    void deleteFailed() {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(1);
        assertThrows(IllegalArgumentException.class, () -> creditCardService.delete(creditCard));
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCardFromDb.getId(), 1);
    }

    @Test
    void deposit() {
        Integer id = 1;
        CreditCard creditCard = creditCardService.create(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(creditCard.getNumber());
        creditCardTransactionDto.setLocale(Locale.getDefault());
        creditCardTransactionDto.setSumOfMoney("1000,35");
        boolean deposit = creditCardService.deposit(creditCardTransactionDto);
        assertTrue(deposit);
    }                   

    @Test
    void transferSucceeded() {
        Integer id = 1;
        CreditCard sourceCreditCard = creditCardService.create(id);
        CreditCard targetCreditCard = creditCardService.create(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setSumOfMoney("1000,00");
        creditCardTransactionDto.setLocale(Locale.getDefault());
        boolean deposit = creditCardService.deposit(creditCardTransactionDto);
        assertTrue(deposit);
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransactionDto.setSumOfMoney("500,00");
        boolean transfer = creditCardService.transfer(creditCardTransactionDto);
        assertTrue(transfer);
    }

    @Test
    void transferFailed() {
        Integer id = 1;
        CreditCard sourceCreditCard = creditCardService.create(id);
        CreditCard targetCreditCard = creditCardService.create(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setSumOfMoney("100,00");
        creditCardTransactionDto.setLocale(Locale.getDefault());
        boolean deposit = creditCardService.deposit(creditCardTransactionDto);
        assertTrue(deposit);
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransactionDto.setSumOfMoney("500,00");
        assertThrows(IllegalArgumentException.class, () -> creditCardService.transfer(creditCardTransactionDto));
        CreditCard sourceCreditCardFromDb = creditCardService.getById(sourceCreditCard.getId());
        CreditCard targetCreditCardFromDb = creditCardService.getById(targetCreditCard.getId());
        assertEquals(sourceCreditCardFromDb.getBalance().intValue(), 100);
        assertEquals(targetCreditCardFromDb.getBalance().intValue(), 0);
    }

    @Test
    void getById() {
        Integer id = 1;
        CreditCard creditCard = creditCardService.create(id);
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCard, creditCardFromDb);
    }
}