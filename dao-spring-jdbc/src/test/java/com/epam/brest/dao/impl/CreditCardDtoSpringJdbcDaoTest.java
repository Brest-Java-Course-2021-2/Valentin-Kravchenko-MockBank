package com.epam.brest.dao.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.dao.CreditCardDao;
import com.epam.brest.dao.CreditCardDtoDao;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.model.entity.CreditCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"classpath*:test-db.xml", "classpath*:spring-jdbc-dao.xml"})
class CreditCardDtoSpringJdbcDaoTest {

    @Autowired
    private CreditCardDtoDao creditCardDtoDao;
    @Autowired
    private CreditCardDao creditCardDao;
    @Autowired
    private BankAccountDao bankAccountDao;

    @Test
    void getAllWithAccountNumber() {
        List<CreditCardDto> cards = creditCardDtoDao.getAllWithAccountNumber();
        assertNotNull(cards);
        CreditCardDto firstCreditCardDto = cards.get(0);
        CreditCardDto lastCreditCardDto = cards.get(cards.size() - 1);
        Optional<CreditCard> firstCreditCardFromDb = creditCardDao.getOneById(firstCreditCardDto.getId());
        Optional<CreditCard> lastCreditCardDtoFromDb = creditCardDao.getOneById(lastCreditCardDto.getId());
        assertEquals(firstCreditCardDto.getNumber(), firstCreditCardFromDb.get().getNumber());
        assertEquals(lastCreditCardDto.getNumber(), lastCreditCardDtoFromDb.get().getNumber());
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getOneById(firstCreditCardDto.getAccountId());
        Optional<BankAccount> lastBankAccountFromDb = bankAccountDao.getOneById(lastCreditCardDto.getAccountId());
        assertEquals(firstCreditCardDto.getAccountNumber(), firstBankAccountFromDb.get().getNumber());
        assertEquals(lastCreditCardDto.getAccountNumber(), lastBankAccountFromDb.get().getNumber());
    }

}