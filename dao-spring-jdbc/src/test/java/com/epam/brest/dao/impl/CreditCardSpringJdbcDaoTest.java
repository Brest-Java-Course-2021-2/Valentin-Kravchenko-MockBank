package com.epam.brest.dao.impl;

import com.epam.brest.dao.CreditCardDao;
import com.epam.brest.model.entity.CreditCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"classpath*:test-db.xml", "classpath*:spring-jdbc-dao.xml"})
class CreditCardSpringJdbcDaoTest {

    @Autowired
    private CreditCardDao creditCardDao;
    private List<CreditCard> cards;
    private CreditCard firstCreditCard;
    private CreditCard lastCreditCard;

    @BeforeEach
    void init() {
        cards = creditCardDao.getAll();
        firstCreditCard = cards.get(0);
        lastCreditCard = cards.get(cards.size() - 1);
    }

    @Test
    void getAll() {
        assertNotNull(cards);
        Integer numberOfAccounts = creditCardDao.count();
        assertEquals(numberOfAccounts, cards.size());
        assertTrue(lastCreditCard.getExpirationDate().isAfter(firstCreditCard.getExpirationDate()));
    }

    @Test
    void getOneById() {
        Optional<CreditCard> firstCreditCardFromDb = creditCardDao.getOneById(firstCreditCard.getId());
        assertEquals(firstCreditCard, firstCreditCardFromDb.get());
        Optional<CreditCard> lastCreditCardFromDb = creditCardDao.getOneById(lastCreditCard.getId());
        assertEquals(lastCreditCard, lastCreditCardFromDb.get());
    }

    @Test
    public void getOneByNonExistingId() {
        Optional<CreditCard> bankAccount = creditCardDao.getOneById(1000);
        assertTrue(bankAccount.isEmpty());
    }

    @Test
    void create() {
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber("New number");
        creditCard.setExpirationDate(LocalDate.now());
        creditCard.setAccountId(1);
        CreditCard newCreditCard = creditCardDao.create(creditCard);
        assertNotNull(newCreditCard.getId());
        Optional<CreditCard> creditCardFromDb = creditCardDao.getOneById(newCreditCard.getId());
        assertEquals(newCreditCard, creditCardFromDb.get());
        assertEquals(creditCardFromDb.get().getBalance().intValue(), 0);
    }

    @Test
    void update() {
        firstCreditCard.setBalance(new BigDecimal("1000.00"));
        Integer result = creditCardDao.update(firstCreditCard);
        assertEquals(result, 1);
        Optional<CreditCard> firstBankAccountFromDb = creditCardDao.getOneById(firstCreditCard.getId());
        assertEquals(firstCreditCard.getBalance(), firstBankAccountFromDb.get().getBalance());
    }

    @Test
    void deleteSucceeded() {
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber("One more new number");
        creditCard.setExpirationDate(LocalDate.now());
        creditCard.setAccountId(1);
        CreditCard newCreditCard = creditCardDao.create(creditCard);
        Integer result = creditCardDao.delete(newCreditCard.getId());
        assertEquals(result, 1);
        Optional<CreditCard> creditCardFromDb = creditCardDao.getOneById(newCreditCard.getId());
        assertTrue(creditCardFromDb.isEmpty());

    }

    @Test
    void deleteFailed() {
        assertThrows(IllegalArgumentException.class, () -> creditCardDao.delete(lastCreditCard.getId()));
        Optional<CreditCard> newCreditCardFromDb = creditCardDao.getOneById(firstCreditCard.getId());
        assertFalse(newCreditCardFromDb.isEmpty());
    }

    @Test
    void isAccountNumberExists() {
        assertTrue(creditCardDao.isCardNumberExists(lastCreditCard.getNumber()));
        assertFalse(creditCardDao.isCardNumberExists("Some number"));
    }

}