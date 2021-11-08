package com.epam.brest.dao.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.model.entity.BankAccount;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"classpath*:test-db.xml", "classpath*:spring-jdbc-dao.xml"})
class SpringJdbcBankAccountDaoTest {

    @Autowired
    private BankAccountDao bankAccountDao;

    private List<BankAccount> accounts;
    private BankAccount firstBankAccount;
    private BankAccount lastBankAccount;

    @BeforeEach
    void init() {
        accounts = bankAccountDao.getAll();
        firstBankAccount = accounts.get(0);
        lastBankAccount = accounts.get(accounts.size() - 1);
    }

    @Test
    void getAll() {
        Integer numberOfAccounts = bankAccountDao.count();
        assertNotNull(accounts);
        assertEquals(numberOfAccounts, accounts.size());
        assertTrue(lastBankAccount.getRegistrationDate().isAfter(firstBankAccount.getRegistrationDate()));
    }

    @Test
    void getOneById() {
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getOneById(firstBankAccount.getAccountId());
        assertEquals(firstBankAccount.getAccountId(), firstBankAccountFromDb.get().getAccountId());
        Optional<BankAccount> lastBankAccountFromDb = bankAccountDao.getOneById(lastBankAccount.getAccountId());
        assertEquals(lastBankAccount.getAccountId(), lastBankAccountFromDb.get().getAccountId());
    }

    @Test
    public void getOneByNonExistingId() {
        Optional<BankAccount> bankAccount = bankAccountDao.getOneById(Integer.MAX_VALUE);
        assertTrue(bankAccount.isEmpty());
    }

    @Test
    void create() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(Iban.random(CountryCode.BY).toString());
        bankAccount.setCustomer("Andrey Andreev");
        bankAccount.setRegistrationDate(LocalDate.now());
        BankAccount newBankAccount = bankAccountDao.create(bankAccount);
        assertNotNull(newBankAccount.getAccountId());
        Optional<BankAccount> bankAccountFromDb = bankAccountDao.getOneById(newBankAccount.getAccountId());
        assertEquals(newBankAccount, bankAccountFromDb.get());
    }

    @Test
    void update() {
        firstBankAccount.setCustomer("New customer");
        Integer result = bankAccountDao.update(firstBankAccount);
        assertEquals(result, 1);
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getOneById(firstBankAccount.getAccountId());
        assertEquals(firstBankAccount.getCustomer(), firstBankAccountFromDb.get().getCustomer());
    }

    @Test
    void deleteSucceeded() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber(Iban.random(CountryCode.BY).toString());
        bankAccount.setCustomer("Andrey Andreev");
        bankAccount.setRegistrationDate(LocalDate.now());
        BankAccount newBankAccount = bankAccountDao.create(bankAccount);
        Integer result = bankAccountDao.delete(newBankAccount.getAccountId());
        assertEquals(result, 1);
        Optional<BankAccount> newBankAccountFromDb = bankAccountDao.getOneById(newBankAccount.getAccountId());
        assertTrue(newBankAccountFromDb.isEmpty());
    }

    @Test
    void deleteFailed() {
        assertThrows(IllegalArgumentException.class, () -> bankAccountDao.delete(firstBankAccount.getAccountId()));
        Optional<BankAccount> newBankAccountFromDb = bankAccountDao.getOneById(firstBankAccount.getAccountId());
        assertFalse(newBankAccountFromDb.isEmpty());
    }

    @Test
    void isAccountNumberExists() {
        assertTrue(bankAccountDao.isAccountNumberExists(firstBankAccount.getAccountNumber()));
        assertFalse(bankAccountDao.isAccountNumberExists(Iban.random().toString()));
    }
    
}