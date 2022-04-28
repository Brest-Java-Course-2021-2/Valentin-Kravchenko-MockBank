package com.epam.brest.dao.impl;

import com.epam.brest.dao.annotation.TestDbDaoIT;
import com.epam.brest.dao.api.BankAccountDao;
import com.epam.brest.faker.api.FakerService;
import com.epam.brest.model.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestDbDaoIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountSpringJdbcDaoIT {

    private final BankAccountDao bankAccountDao;

    private final List<BankAccount> accounts;

    private final BankAccount firstBankAccount;

    private final BankAccount lastBankAccount;

    private final BankAccount fakeBankAccount;

    public BankAccountSpringJdbcDaoIT(BankAccountDao bankAccountDao, FakerService<BankAccount> fakerService) {
        this.bankAccountDao = bankAccountDao;
        this.accounts = bankAccountDao.getAll();
        this.firstBankAccount = accounts.get(0);
        this.lastBankAccount = accounts.get(accounts.size() - 1);
        this.fakeBankAccount = fakerService.getFakeData().get(0);
    }

    @Test
    void getAll() {
        assertNotNull(accounts);
        Integer accountsCount = bankAccountDao.count();
        assertEquals(accountsCount, accounts.size());
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
        BankAccount bankAccount = bankAccountDao.create(fakeBankAccount);
        Optional<BankAccount> bankAccountFromDb = bankAccountDao.getById(bankAccount.getId());
        assertEquals(bankAccount, bankAccountFromDb.get());
    }

    @Test
    void update() {
        firstBankAccount.setCustomer(fakeBankAccount.getCustomer());
        Integer result = bankAccountDao.update(firstBankAccount);
        assertEquals(result, 1);
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getById(firstBankAccount.getId());
        assertEquals(firstBankAccount.getCustomer(), firstBankAccountFromDb.get().getCustomer());
    }

    @Test
    void delete() {
        BankAccount bankAccount = bankAccountDao.create(fakeBankAccount);
        Integer id = bankAccountDao.delete(bankAccount.getId());
        assertEquals(bankAccount.getId(), id);
        Optional<BankAccount> bankAccountFromDb = bankAccountDao.getById(id);
        assertTrue(bankAccountFromDb.isEmpty());
    }

    @Test
    void isAccountNumberExists() {
        assertTrue(bankAccountDao.isAccountNumberExists(firstBankAccount.getNumber()));
        assertFalse(bankAccountDao.isAccountNumberExists(fakeBankAccount.getNumber()));
    }

}