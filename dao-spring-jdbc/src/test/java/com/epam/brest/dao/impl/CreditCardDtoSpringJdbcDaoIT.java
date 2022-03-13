package com.epam.brest.dao.impl;

import com.epam.brest.dao.api.BankAccountDao;
import com.epam.brest.dao.api.CreditCardDao;
import com.epam.brest.dao.api.CreditCardDtoDao;
import com.epam.brest.model.dto.CreditCardsFilterDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreditCardDtoSpringJdbcDaoIT extends DaoTestBasic {

    private final CreditCardDtoDao creditCardDtoDao;
    private final CreditCardDao creditCardDao;
    private final BankAccountDao bankAccountDao;

    private List<CreditCardDto> cards;
    private CreditCardDto firstCreditCard;
    private CreditCardDto lastCreditCard;

    public CreditCardDtoSpringJdbcDaoIT(@Autowired CreditCardDtoDao creditCardDtoDao,
                                        @Autowired CreditCardDao creditCardDao,
                                        @Autowired BankAccountDao bankAccountDao) {
        this.creditCardDtoDao = creditCardDtoDao;
        this.creditCardDao = creditCardDao;
        this.bankAccountDao = bankAccountDao;
    }

    @BeforeEach
    void setup(){
        cards = creditCardDtoDao.getAllWithAccountNumber();
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
        CreditCardsFilterDto creditCardsFilterDto = new CreditCardsFilterDto();
        creditCardsFilterDto.setFromDate(firstCreditCard.getExpirationDate());
        creditCardsFilterDto.setToDate(lastCreditCard.getExpirationDate());
        List<CreditCardDto> cards = creditCardDtoDao.getAllWithAccountNumber(creditCardsFilterDto);
        assertNotNull(cards);
        assertEquals(cards.get(0).getExpirationDate(), firstCreditCard.getExpirationDate());
        assertEquals(cards.get(cards.size() - 1).getExpirationDate(), lastCreditCard.getExpirationDate());
        //Case 2
        creditCardsFilterDto.setToDate(null);
        cards = creditCardDtoDao.getAllWithAccountNumber(creditCardsFilterDto);
        assertNotNull(cards);
        assertEquals(cards.get(0).getExpirationDate(), firstCreditCard.getExpirationDate());
        assertEquals(cards.get(cards.size() - 1).getExpirationDate(), lastCreditCard.getExpirationDate());
        //Case 3
        creditCardsFilterDto.setFromDate(null);
        creditCardsFilterDto.setToDate(lastCreditCard.getExpirationDate());
        cards = creditCardDtoDao.getAllWithAccountNumber(creditCardsFilterDto);
        assertNotNull(cards);
        assertEquals(cards.get(0).getExpirationDate(), firstCreditCard.getExpirationDate());
        assertEquals(cards.get(cards.size() - 1).getExpirationDate(), lastCreditCard.getExpirationDate());
    }

}