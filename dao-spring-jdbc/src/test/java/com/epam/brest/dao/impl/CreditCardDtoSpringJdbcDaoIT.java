package com.epam.brest.dao.impl;

import com.epam.brest.dao.annotation.TestDbDaoIT;
import com.epam.brest.dao.api.BankAccountDao;
import com.epam.brest.dao.api.CreditCardDao;
import com.epam.brest.dao.api.CreditCardDtoDao;
import com.epam.brest.model.CreditCardDto;
import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.model.BankAccount;
import com.epam.brest.model.CreditCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestDbDaoIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditCardDtoSpringJdbcDaoIT {

    private final CreditCardDtoDao creditCardDtoDao;
    private final CreditCardDao creditCardDao;
    private final BankAccountDao bankAccountDao;

    private CreditCardDto firstCreditCard;
    private CreditCardDto lastCreditCard;

    public CreditCardDtoSpringJdbcDaoIT(CreditCardDtoDao creditCardDtoDao,
                                        CreditCardDao creditCardDao,
                                        BankAccountDao bankAccountDao) {
        this.creditCardDtoDao = creditCardDtoDao;
        this.creditCardDao = creditCardDao;
        this.bankAccountDao = bankAccountDao;
    }

    @BeforeEach
    void setup(){
        List<CreditCardDto> cards = creditCardDtoDao.getAllWithAccountNumber();
        firstCreditCard = cards.get(0);
        lastCreditCard = cards.get(cards.size() - 1);
    }

    @Test
    void getAllWithAccountNumber() {
        Optional<CreditCard> firstCreditCardFromDb = creditCardDao.getById(firstCreditCard.getId());
        Optional<CreditCard> lastCreditCardDtoFromDb = creditCardDao.getById(lastCreditCard.getId());
        assertEquals(firstCreditCard.getNumber(), firstCreditCardFromDb.get().getNumber());
        assertEquals(lastCreditCard.getNumber(), lastCreditCardDtoFromDb.get().getNumber());
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getById(firstCreditCard.getAccountId());
        Optional<BankAccount> lastBankAccountFromDb = bankAccountDao.getById(lastCreditCard.getAccountId());
        assertEquals(firstCreditCard.getAccountNumber(), firstBankAccountFromDb.get().getNumber());
        assertEquals(lastCreditCard.getAccountNumber(), lastBankAccountFromDb.get().getNumber());
    }

    @Test
    void getGetAllWithAccountNumberByDateRange() {
        //Case 1
        CreditCardFilterDto creditCardFilterDto = new CreditCardFilterDto();
        creditCardFilterDto.setFromDate(firstCreditCard.getExpirationDate());
        creditCardFilterDto.setToDate(lastCreditCard.getExpirationDate());
        List<CreditCardDto> cards = creditCardDtoDao.getAllWithAccountNumber(creditCardFilterDto);
        assertNotNull(cards);
        assertEquals(cards.get(0).getExpirationDate(), firstCreditCard.getExpirationDate());
        assertEquals(cards.get(cards.size() - 1).getExpirationDate(), lastCreditCard.getExpirationDate());
        //Case 2
        creditCardFilterDto.setToDate(null);
        cards = creditCardDtoDao.getAllWithAccountNumber(creditCardFilterDto);
        assertNotNull(cards);
        assertEquals(cards.get(0).getExpirationDate(), firstCreditCard.getExpirationDate());
        assertEquals(cards.get(cards.size() - 1).getExpirationDate(), lastCreditCard.getExpirationDate());
        //Case 3
        creditCardFilterDto.setFromDate(null);
        creditCardFilterDto.setToDate(lastCreditCard.getExpirationDate());
        cards = creditCardDtoDao.getAllWithAccountNumber(creditCardFilterDto);
        assertNotNull(cards);
        assertEquals(cards.get(0).getExpirationDate(), firstCreditCard.getExpirationDate());
        assertEquals(cards.get(cards.size() - 1).getExpirationDate(), lastCreditCard.getExpirationDate());
    }

}