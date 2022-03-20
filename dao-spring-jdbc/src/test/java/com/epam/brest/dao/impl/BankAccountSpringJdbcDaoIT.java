package com.epam.brest.dao.impl;

import com.epam.brest.dao.annotation.DaoIT;
import com.epam.brest.dao.api.BankAccountDao;
import com.epam.brest.model.entity.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DaoIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountSpringJdbcDaoIT {

    private final BankAccountDao bankAccountDao;
    private List<BankAccount> accounts;
    private BankAccount firstBankAccount;
    private BankAccount lastBankAccount;

    public BankAccountSpringJdbcDaoIT(BankAccountDao bankAccountDao) {
        this.bankAccountDao = bankAccountDao;
    }

    @BeforeEach
    void setup() {
        accounts = bankAccountDao.getAll();
        firstBankAccount = accounts.get(0);
        lastBankAccount = accounts.get(accounts.size() - 1);
    }

    @Test
    void getAll() {
        assertNotNull(accounts);
        Integer numberOfAccounts = bankAccountDao.count();
        assertEquals(numberOfAccounts, accounts.size());
        assertTrue(lastBankAccount.getRegistrationDate().isAfter(firstBankAccount.getRegistrationDate()));
    }

    @Test
    void getById() {
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getById(firstBankAccount.getId());
        assertEquals(firstBankAccount, firstBankAccountFromDb.get());
        Optional<BankAccount> lastBankAccountFromDb = bankAccountDao.getById(lastBankAccount.getId());
        assertEquals(lastBankAccount, lastBankAccountFromDb.get());
    }

    @Test
    void getByNumber() {
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getByNumber(firstBankAccount.getNumber());
        assertEquals(firstBankAccount, firstBankAccountFromDb.get());
        Optional<BankAccount> lastBankAccountFromDb = bankAccountDao.getByNumber(lastBankAccount.getNumber());
        assertEquals(lastBankAccount, lastBankAccountFromDb.get());
    }

    @Test
    public void getByNonExistingId() {
        Optional<BankAccount> bankAccount = bankAccountDao.getById(1000);
        assertTrue(bankAccount.isEmpty());
    }

    @Test
    void create() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setNumber("New number");
        bankAccount.setCustomer("New customer");
        bankAccount.setRegistrationDate(LocalDate.now());
        BankAccount newBankAccount = bankAccountDao.create(bankAccount);
        assertNotNull(newBankAccount.getId());
        Optional<BankAccount> bankAccountFromDb = bankAccountDao.getById(newBankAccount.getId());
        assertEquals(newBankAccount, bankAccountFromDb.get());
    }

    @Test
    void update() {
        firstBankAccount.setCustomer("New customer");
        Integer result = bankAccountDao.update(firstBankAccount);
        assertEquals(result, 1);
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getById(firstBankAccount.getId());
        assertEquals(firstBankAccount.getCustomer(), firstBankAccountFromDb.get().getCustomer());
    }

    @Test
    void delete() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setNumber("New number");
        bankAccount.setCustomer("New customer");
        bankAccount.setRegistrationDate(LocalDate.now());
        BankAccount newBankAccount = bankAccountDao.create(bankAccount);
        Integer result = bankAccountDao.delete(newBankAccount.getId());
        assertEquals(result, 1);
        Optional<BankAccount> newBankAccountFromDb = bankAccountDao.getById(newBankAccount.getId());
        assertTrue(newBankAccountFromDb.isEmpty());
    }

    @Test
    void isAccountNumberExists() {
        assertTrue(bankAccountDao.isAccountNumberExists(firstBankAccount.getNumber()));
        assertFalse(bankAccountDao.isAccountNumberExists("New number"));
    }

}