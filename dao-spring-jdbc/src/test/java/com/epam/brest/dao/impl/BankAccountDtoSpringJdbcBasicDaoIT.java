package com.epam.brest.dao.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.dao.BankAccountDtoDao;
import com.epam.brest.dao.BasicDaoTest;
import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.entity.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BankAccountDtoSpringJdbcBasicDaoIT extends BasicDaoTest {

    private final BankAccountDtoDao bankAccountDtoDao;
    private final BankAccountDao bankAccountDao;

    public BankAccountDtoSpringJdbcBasicDaoIT(@Autowired BankAccountDtoDao bankAccountDtoDao,
                                              @Autowired BankAccountDao bankAccountDao) {
        this.bankAccountDtoDao = bankAccountDtoDao;
        this.bankAccountDao = bankAccountDao;
    }

    @Test
    void getAllWithTotalCards() {
        List<BankAccountDto> accountDtos = bankAccountDtoDao.getAllWithTotalCards();
        assertNotNull(accountDtos);
        BankAccountDto firstBankAccountDto = accountDtos.get(0);
        BankAccountDto lastBankAccountDto = accountDtos.get(accountDtos.size() - 1);
        Optional<BankAccount> firstBankAccountFromDb = bankAccountDao.getById(firstBankAccountDto.getId());
        Optional<BankAccount> lastBankAccountFromDb = bankAccountDao.getById(lastBankAccountDto.getId());
        assertEquals(firstBankAccountDto.getId(), firstBankAccountFromDb.get().getId());
        assertEquals(firstBankAccountDto.getCustomer(), firstBankAccountFromDb.get().getCustomer());
        assertEquals(firstBankAccountDto.getRegistrationDate(), firstBankAccountFromDb.get().getRegistrationDate());
        assertEquals(lastBankAccountDto.getId(), lastBankAccountFromDb.get().getId());
        assertEquals(lastBankAccountDto.getCustomer(), lastBankAccountFromDb.get().getCustomer());
        assertEquals(lastBankAccountDto.getRegistrationDate(), lastBankAccountFromDb.get().getRegistrationDate());
        assertTrue(firstBankAccountDto.getTotalCards() > 0);
        assertTrue(lastBankAccountDto.getTotalCards() > 0);
    }

    @Test
    void createNewAccountThenGetAllWithTotalCards() {
        List<BankAccountDto> accountDtos = bankAccountDtoDao.getAllWithTotalCards();
        assertNotNull(accountDtos);
        int countBefore = accountDtos.size();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setNumber("New number");
        bankAccount.setCustomer("New customer");
        bankAccount.setRegistrationDate(LocalDate.now());
        bankAccountDao.create(bankAccount);
        accountDtos = bankAccountDtoDao.getAllWithTotalCards();
        assertNotNull(accountDtos);
        int countAfter = accountDtos.size();
        assertEquals(countBefore, countAfter - 1);
    }

}