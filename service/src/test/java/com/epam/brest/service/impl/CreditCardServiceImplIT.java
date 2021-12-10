package com.epam.brest.service.impl;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import com.epam.brest.service.exception.CreditCardException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardServiceImplIT extends ServiceTestConfiguration {

    private final CreditCardService creditCardService;

    public CreditCardServiceImplIT(@Autowired CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Test
    void create() {
        Integer accountId = 1;
        CreditCard creditCard = creditCardService.create(1);
        assertNotNull(creditCard.getId());
        assertNotNull(creditCard.getNumber());
        assertEquals(creditCard.getBalance().intValue(), 0);
        assertEquals(creditCard.getAccountId(), accountId);
    }

    @Test
    void deleteSucceeded() {
        Integer id = 1;
        CreditCard creditCard = creditCardService.create(id);
        CreditCard deletedCreditCard = creditCardService.delete(creditCard.getId());
        assertEquals(creditCard, deletedCreditCard);
    }

    @Test
    void deleteFailed() {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(1);
        assertThrows(CreditCardException.class, () -> creditCardService.delete(creditCard.getId()));
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCardFromDb.getId(), 1);
    }

    @Test
    void deposit() {
        Integer id = 1;
        CreditCard creditCard = creditCardService.create(id);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(creditCard.getNumber());
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("1000.35"));
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
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("1000.00"));
        boolean deposit = creditCardService.deposit(creditCardTransactionDto);
        assertTrue(deposit);
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("500.00"));
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
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("100.00"));
        boolean deposit = creditCardService.deposit(creditCardTransactionDto);
        assertTrue(deposit);
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("500.00"));
        assertThrows(CreditCardException.class, () -> creditCardService.transfer(creditCardTransactionDto));
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

    @Test
    void getByNumber() {
        Integer id = 1;
        CreditCard creditCard = creditCardService.create(id);
        CreditCard creditCardFromDb = creditCardService.getByNumber(creditCard.getNumber());
        assertEquals(creditCard, creditCardFromDb);
    }

    @Test
    void getAllByAccountId() {
        Integer accountId = 1;
        List<CreditCard> cards = creditCardService.getAllByAccountId(accountId);
        assertNotNull(cards);
        assertTrue(cards.size() > 0);
        assertEquals(cards.get(0).getAccountId(), accountId);
        assertEquals(cards.get(cards.size() - 1).getAccountId(), accountId);
    }
    
}