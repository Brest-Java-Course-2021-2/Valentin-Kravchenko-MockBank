package com.epam.brest.dao.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.dao.CreditCardDao;
import com.epam.brest.dao.CreditCardDtoDao;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreditCardDtoSpringJdbcDaoIT extends BasicDaoIT {

    private final CreditCardDtoDao creditCardDtoDao;
    private final CreditCardDao creditCardDao;
    private final BankAccountDao bankAccountDao;

    public CreditCardDtoSpringJdbcDaoIT(@Autowired CreditCardDtoDao creditCardDtoDao,
                                        @Autowired CreditCardDao creditCardDao,
                                        @Autowired BankAccountDao bankAccountDao) {
        this.creditCardDtoDao = creditCardDtoDao;
        this.creditCardDao = creditCardDao;
        this.bankAccountDao = bankAccountDao;
    }

    @Test
    void getAllWithAccountNumber() {
        List<CreditCardDto> cards = creditCardDtoDao.getAllWithAccountNumber();
        assertNotNull(cards);
        CreditCardDto firstCreditCardDto = cards.get(0);
        CreditCardDto lastCreditCardDto = cards.get(cards.size() - 1);
        Optional<CreditCard> firstCreditCardFromDb = creditCardDao.getById(firstCreditCardDto.getId());
        Optional<CreditCard> lastCreditCardDtoFromDb = creditCardDao.getById(lastCreditCardDto.getId());
        assertEquals(firstCreditCardDto.getNumber(), firstCreditCardFromDb.get().getNumber());
        assertEquals(lastCreditCardDto.getNumber(), lastCreditCardDtoFromDb.get().getNumber());
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getById(firstCreditCardDto.getAccountId());
        Optional<BankAccount> lastBankAccountFromDb = bankAccountDao.getById(lastCreditCardDto.getAccountId());
        assertEquals(firstCreditCardDto.getAccountNumber(), firstBankAccountFromDb.get().getNumber());
        assertEquals(lastCreditCardDto.getAccountNumber(), lastBankAccountFromDb.get().getNumber());
    }

}