package com.epam.brest.dao.impl;

import com.epam.brest.dao.BasicDaoTest;
import com.epam.brest.dao.CreditCardDao;
import com.epam.brest.model.entity.CreditCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardSpringJdbcDaoIT extends BasicDaoTest {

    private final CreditCardDao creditCardDao;
    private List<CreditCard> cards;
    private CreditCard firstCreditCard;
    private CreditCard lastCreditCard;

    public CreditCardSpringJdbcDaoIT(@Autowired CreditCardDao creditCardDao) {
        this.creditCardDao = creditCardDao;
    }

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
    void getById() {
        Optional<CreditCard> firstCreditCardFromDb = creditCardDao.getById(firstCreditCard.getId());
        assertEquals(firstCreditCard, firstCreditCardFromDb.get());
        Optional<CreditCard> lastCreditCardFromDb = creditCardDao.getById(lastCreditCard.getId());
        assertEquals(lastCreditCard, lastCreditCardFromDb.get());
    }

    @Test
    void getByNumber() {
        Optional<CreditCard> firstCreditCardFromDb = creditCardDao.getByNumber(firstCreditCard.getNumber());
        assertEquals(firstCreditCard, firstCreditCardFromDb.get());
        Optional<CreditCard> lastCreditCardFromDb = creditCardDao.getByNumber(lastCreditCard.getNumber());
        assertEquals(lastCreditCard, lastCreditCardFromDb.get());
    }

    @Test
    public void getOneByNonExistingId() {
        Optional<CreditCard> bankAccount = creditCardDao.getById(1000);
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
        Optional<CreditCard> creditCardFromDb = creditCardDao.getById(newCreditCard.getId());
        assertEquals(newCreditCard, creditCardFromDb.get());
        assertEquals(creditCardFromDb.get().getBalance().intValue(), 0);
    }

    @Test
    void update() {
        firstCreditCard.setBalance(new BigDecimal("1000.00"));
        Integer result = creditCardDao.update(firstCreditCard);
        assertEquals(result, 1);
        Optional<CreditCard> firstBankAccountFromDb = creditCardDao.getById(firstCreditCard.getId());
        assertEquals(firstCreditCard.getBalance(), firstBankAccountFromDb.get().getBalance());
    }

    @Test
    void deleteSucceeded() {
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber("New number");
        creditCard.setExpirationDate(LocalDate.now());
        creditCard.setBalance(new BigDecimal("0.00"));
        creditCard.setAccountId(1);
        CreditCard newCreditCard = creditCardDao.create(creditCard);
        Integer result = creditCardDao.delete(newCreditCard);
        assertEquals(result, 1);
        Optional<CreditCard> creditCardFromDb = creditCardDao.getById(newCreditCard.getId());
        assertTrue(creditCardFromDb.isEmpty());

    }

    @Test
    void deleteFailed() {
        assertThrows(IllegalArgumentException.class, () -> creditCardDao.delete(lastCreditCard));
        Optional<CreditCard> newCreditCardFromDb = creditCardDao.getById(firstCreditCard.getId());
        assertFalse(newCreditCardFromDb.isEmpty());
    }

    @Test
    void isAccountNumberExists() {
        assertTrue(creditCardDao.isCardNumberExists(lastCreditCard.getNumber()));
        assertFalse(creditCardDao.isCardNumberExists("New number"));
    }

}