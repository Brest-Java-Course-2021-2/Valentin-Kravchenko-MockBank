package com.epam.brest.impl;

import com.epam.brest.BasicServiceTest;
import com.epam.brest.model.dto.CreditCardDepositDto;
import com.epam.brest.model.dto.CreditCardTransferDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardServiceImplIT extends BasicServiceTest {

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

    @Test
    void deposit() {
        Integer id = 1;
        CreditCard creditCard = creditCardService.create(id);
        CreditCardDepositDto creditCardDepositDto = new CreditCardDepositDto();
        creditCardDepositDto.setCardNumber(creditCard.getNumber());
        creditCardDepositDto.setSumOfMoney(new BigDecimal("1000.35"));
        boolean deposit = creditCardService.deposit(creditCardDepositDto);
        assertTrue(deposit);
    }                   

    @Test
    void transferSucceeded() {
        Integer id = 1;
        CreditCard sourceCreditCard = creditCardService.create(id);
        CreditCard targetCreditCard = creditCardService.create(id);
        CreditCardDepositDto creditCardDepositDto = new CreditCardDepositDto();
        creditCardDepositDto.setCardNumber(sourceCreditCard.getNumber());
        creditCardDepositDto.setSumOfMoney(new BigDecimal("1000.00"));
        boolean deposit = creditCardService.deposit(creditCardDepositDto);
        assertTrue(deposit);
        CreditCardTransferDto creditCardTransferDto = new CreditCardTransferDto();
        creditCardTransferDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransferDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransferDto.setSumOfMoney(new BigDecimal("500.00"));
        boolean transfer = creditCardService.transfer(creditCardTransferDto);
        assertTrue(transfer);
    }

    @Test
    void transferFailed() {
        Integer id = 1;
        CreditCard sourceCreditCard = creditCardService.create(id);
        CreditCard targetCreditCard = creditCardService.create(id);
        CreditCardDepositDto creditCardDepositDto = new CreditCardDepositDto();
        creditCardDepositDto.setCardNumber(sourceCreditCard.getNumber());
        creditCardDepositDto.setSumOfMoney(new BigDecimal("100.00"));
        boolean deposit = creditCardService.deposit(creditCardDepositDto);
        assertTrue(deposit);
        CreditCardTransferDto creditCardTransferDto = new CreditCardTransferDto();
        creditCardTransferDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransferDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransferDto.setSumOfMoney(new BigDecimal("500.00"));
        boolean transfer = creditCardService.transfer(creditCardTransferDto);
        assertFalse(transfer);
    }

}