package com.epam.brest.service.impl;

import com.epam.brest.model.CreditCardTransactionDto;
import com.epam.brest.model.CreditCard;
import com.epam.brest.service.annotation.ServiceIT;
import com.epam.brest.service.api.ExtendedCreditCardService;
import com.epam.brest.service.exception.CreditCardException;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ServiceIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditCardServiceImplIT {

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
    void remove() {
        CreditCard creditCard = creditCardService.create(1);
        CreditCard deletedCreditCard = creditCardService.delete(creditCard.getId());
        assertEquals(creditCard, deletedCreditCard);
    }

    @Test
    void failedRemove() {
        CreditCard creditCard = new CreditCard();
        int id = 1;
        creditCard.setId(id);
        assertThrows(CreditCardException.class, () -> creditCardService.delete(creditCard.getId()));
        CreditCard creditCardFromDb = creditCardService.getById(creditCard.getId());
        assertEquals(creditCardFromDb.getId(), id);
    }

    @Test
    void deposit() {
        CreditCard creditCard = creditCardService.create(1);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(creditCard.getNumber());
        BigDecimal sumOfMoney = new BigDecimal("1560.35");
        creditCardTransactionDto.setSumOfMoney(sumOfMoney);
        CreditCard creditCardAfterTransaction = creditCardService.deposit(creditCardTransactionDto);
        assertEquals(creditCardAfterTransaction.getBalance(), sumOfMoney);
    }                   

    @Test
    void transfer() {
        CreditCard sourceCreditCard = creditCardService.create(1);
        CreditCard targetCreditCard = creditCardService.create(1);
        // Deposit transaction
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setTargetCardNumber(sourceCreditCard.getNumber());
        BigDecimal depositSumOfMoney = new BigDecimal("1000.00");
        creditCardTransactionDto.setSumOfMoney(depositSumOfMoney);
        CreditCard sourceCreditCardAfterDeposit = creditCardService.deposit(creditCardTransactionDto);
        assertEquals(sourceCreditCardAfterDeposit.getBalance(), depositSumOfMoney);
        // Transfer transaction
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        BigDecimal transferSumOfMoney = new BigDecimal("500.35");
        creditCardTransactionDto.setSumOfMoney(transferSumOfMoney);
        CreditCard sourceCreditCardAfterTransfer = creditCardService.transfer(creditCardTransactionDto);
        assertEquals(sourceCreditCardAfterDeposit.getBalance().subtract(sourceCreditCardAfterTransfer.getBalance()),
                     transferSumOfMoney);
        CreditCard targetCreditCardFromDb = creditCardService.getById(targetCreditCard.getId());
        assertEquals(targetCreditCardFromDb.getBalance(), transferSumOfMoney);
    }

    @Test
    void failedTransfer() {
        CreditCard sourceCreditCard = creditCardService.create(1);
        CreditCard targetCreditCard = creditCardService.create(1);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(sourceCreditCard.getNumber());
        creditCardTransactionDto.setTargetCardNumber(targetCreditCard.getNumber());
        creditCardTransactionDto.setSumOfMoney(new BigDecimal("500.00"));
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