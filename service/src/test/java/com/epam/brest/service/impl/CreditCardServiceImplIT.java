package com.epam.brest.service.impl;

import com.epam.brest.model.CreditCardTransactionDto;
import com.epam.brest.model.CreditCard;
import com.epam.brest.service.annotation.ServiceIT;
import com.epam.brest.service.api.ExtendedCreditCardService;
import com.epam.brest.service.exception.CreditCardException;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ServiceIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditCardServiceImplIT {

    public static final String RU = "ru";
    public static final String EN = "en";
    public static final String TEST_VALUE_RU = "540,65";
    public static final String TEST_VALUE_EN = "540.65";

    private final ExtendedCreditCardService creditCardService;

    public CreditCardServiceImplIT(ExtendedCreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Test
    void create() {
        Integer accountId = 1;
        CreditCard creditCard = creditCardService.create(accountId);
        assertNotNull(creditCard.getId());
        assertNotNull(creditCard.getNumber());
        assertEquals(creditCard.getBalance().intValue(), 0);
        assertEquals(creditCard.getAccountId(), accountId);
    }

    @Test
    void delete() {
        CreditCard creditCard = creditCardService.create(1);
        CreditCard deletedCreditCard = creditCardService.delete(creditCard.getId());
        assertEquals(creditCard, deletedCreditCard);
    }

    @Test
    void deleteFail() {
        CreditCard creditCard = new CreditCard();
        int id = 1;
        creditCard.setId(id);
        assertThrows(CreditCardException.class, () -> creditCardService.delete(creditCard.getId()));
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCardFromDb.getId(), id);
    }

    @Test
    void deposit() {
        CreditCard targetCreditCard = creditCardService.create(1);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransactionDto.setTransactionAmountValue(TEST_VALUE_RU);
        creditCardTransactionDto.setLocale(new Locale(RU));
        CreditCard targetCreditCardAfterTransaction = creditCardService.deposit(creditCardTransactionDto);
        assertEquals(targetCreditCardAfterTransaction.getBalance(), new BigDecimal(TEST_VALUE_EN));
    }                   

    @Test
    void transfer() {
        CreditCard sourceCreditCard = creditCardService.create(1);
        CreditCard targetCreditCard = creditCardService.create(1);
        // Deposit transaction
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTransactionAmountValue(TEST_VALUE_EN);
        creditCardTransactionDto.setLocale(new Locale(EN));
        CreditCard sourceCreditCardAfterDeposit = creditCardService.deposit(creditCardTransactionDto);
        assertEquals(sourceCreditCardAfterDeposit.getBalance(), new BigDecimal(TEST_VALUE_EN));
        // Transfer transaction
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTransactionAmountValue(TEST_VALUE_RU);
        creditCardTransactionDto.setLocale(new Locale(RU));
        CreditCard sourceCreditCardAfterTransfer = creditCardService.transfer(creditCardTransactionDto);
        assertEquals(sourceCreditCardAfterTransfer.getBalance().intValue(), 0);
        CreditCard targetCreditCardAfterTransfer = creditCardService.getById(targetCreditCard.getId());
        assertEquals(targetCreditCardAfterTransfer.getBalance(), new BigDecimal(TEST_VALUE_EN));
    }

    @Test
    void transferFail() {
        CreditCard sourceCreditCard = creditCardService.create(1);
        CreditCard targetCreditCard = creditCardService.create(1);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransactionDto.setTransactionAmountValue(TEST_VALUE_RU);
        creditCardTransactionDto.setLocale(new Locale(RU));
        assertThrows(CreditCardException.class, () -> creditCardService.transfer(creditCardTransactionDto));
        CreditCard sourceCreditCardFromDb = creditCardService.getById(sourceCreditCard.getId());
        CreditCard targetCreditCardFromDb = creditCardService.getById(targetCreditCard.getId());
        assertEquals(sourceCreditCardFromDb.getBalance().intValue(), 0);
        assertEquals(targetCreditCardFromDb.getBalance().intValue(), 0);
    }

    @Test
    void getById() {
        CreditCard creditCard = creditCardService.create(1);
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCard, creditCardFromDb);
    }

    @Test
    void getByNumber() {
        CreditCard creditCard = creditCardService.create(1);
        CreditCard creditCardFromDb = creditCardService.getByNumber(creditCard.getNumber());
        assertEquals(creditCard, creditCardFromDb);
    }

}