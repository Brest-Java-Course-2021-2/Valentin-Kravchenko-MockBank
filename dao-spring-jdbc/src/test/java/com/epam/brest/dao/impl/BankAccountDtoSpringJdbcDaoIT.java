package com.epam.brest.dao.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.dao.BankAccountDtoDao;
import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.model.entity.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountDtoSpringJdbcDaoIT extends DaoTestConfiguration {

    private final BankAccountDtoDao bankAccountDtoDao;
    private final BankAccountDao bankAccountDao;

    public BankAccountDtoSpringJdbcDaoIT(@Autowired BankAccountDtoDao bankAccountDtoDao,
                                         @Autowired BankAccountDao bankAccountDao) {
        this.bankAccountDtoDao = bankAccountDtoDao;
        this.bankAccountDao = bankAccountDao;
    }

    @Test
    void getAllWithTotalCards() {
        List<BankAccountDto> accounts = bankAccountDtoDao.getAllWithTotalCards();
        assertNotNull(accounts);
        BankAccountDto firstBankAccountDto = accounts.get(0);
        BankAccountDto lastBankAccountDto = accounts.get(accounts.size() - 1);
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
    void getAllWithTotalCardsAfterCreateNewAccount() {
        List<BankAccountDto> accounts = bankAccountDtoDao.getAllWithTotalCards();
        assertNotNull(accounts);
        int countBefore = accounts.size();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setNumber("New number");
        bankAccount.setCustomer("New customer");
        bankAccount.setRegistrationDate(LocalDate.now());
        bankAccountDao.create(bankAccount);
        accounts = bankAccountDtoDao.getAllWithTotalCards();
        assertNotNull(accounts);
        int countAfter = accounts.size();
        assertEquals(countBefore, countAfter - 1);
    }

    @Test
    void getAllWithTotalCardsByFilter() {
        //Case 1
        BankAccountFilterDto bankAccountFilterDto = new BankAccountFilterDto();
        String number = "S8416E1PX";
        String customer = "vano";
        bankAccountFilterDto.setNumberPattern(number);
        bankAccountFilterDto.setCustomerPattern(customer);
        List<BankAccountDto> accounts = bankAccountDtoDao.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.get(0).getNumber().contains(number));
        assertTrue(accounts.get(0).getCustomer().contains(customer));
        //case 2
        bankAccountFilterDto.setCustomerPattern(null);
        accounts = bankAccountDtoDao.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.get(0).getNumber().contains(number));
        //case 3
        customer = "Cusmoter";
        bankAccountFilterDto.setNumberPattern(null);
        bankAccountFilterDto.setCustomerPattern(customer);
        accounts = bankAccountDtoDao.getAllWithTotalCards(bankAccountFilterDto);
        assertEquals(accounts.size(), 0);
    }

}