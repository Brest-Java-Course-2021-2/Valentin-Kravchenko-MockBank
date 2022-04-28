package com.epam.brest.dao.impl;

import com.epam.brest.dao.annotation.TestDbDaoIT;
import com.epam.brest.dao.api.CreditCardDao;
import com.epam.brest.faker.api.FakerService;
import com.epam.brest.model.CreditCard;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestDbDaoIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditCardSpringJdbcDaoIT {

    private final CreditCardDao creditCardDao;

    private final List<CreditCard> cards;

    private final CreditCard firstCreditCard;

    private final CreditCard lastCreditCard;

    private final CreditCard fakeCreditCard;

    public CreditCardSpringJdbcDaoIT(CreditCardDao creditCardDao, FakerService<CreditCard> fakerService) {
        this.creditCardDao = creditCardDao;
        this.cards = creditCardDao.getAll();
        this.firstCreditCard = cards.get(0);
        this.lastCreditCard = cards.get(cards.size() - 1);
        this.fakeCreditCard = fakerService.getFakeData().get(0);
    }

    @Test
    void getAll() {
        assertNotNull(cards);
        Integer cardsCount = creditCardDao.count();
        assertEquals(cardsCount, cards.size());
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
        CreditCard creditCard = creditCardDao.create(fakeCreditCard);
        Optional<CreditCard> creditCardFromDb = creditCardDao.getById(creditCard.getId());
        assertEquals(creditCard, creditCardFromDb.get());
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
    void delete() {
        CreditCard creditCard = creditCardDao.create(fakeCreditCard);
        Integer id = creditCardDao.delete(creditCard.getId());
        assertEquals(creditCard.getId(), id);
        Optional<CreditCard> creditCardFromDb = creditCardDao.getById(creditCard.getId());
        assertTrue(creditCardFromDb.isEmpty());

    }

    @Test
    void isAccountNumberExists() {
        assertTrue(creditCardDao.isCardNumberExists(lastCreditCard.getNumber()));
        assertFalse(creditCardDao.isCardNumberExists(fakeCreditCard.getNumber()));
    }

    @Test
    void getAllByAccountId() {
        List<CreditCard> cardsByAccountId = creditCardDao.getAllByAccountId(firstCreditCard.getAccountId());
        assertTrue(cardsByAccountId.size() > 0);
        assertEquals(cardsByAccountId.get(0).getAccountId(), firstCreditCard.getAccountId());
        assertEquals(cardsByAccountId.get(cardsByAccountId.size() - 1).getAccountId(), firstCreditCard.getAccountId());
    }

}