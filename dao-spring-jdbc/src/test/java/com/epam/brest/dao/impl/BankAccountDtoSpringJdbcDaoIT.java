package com.epam.brest.dao.impl;

import com.epam.brest.dao.annotation.DaoITWithProdDb;
import com.epam.brest.dao.api.BankAccountDao;
import com.epam.brest.dao.api.BankAccountDtoDao;
import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountsFilterDto;
import com.epam.brest.model.entity.BankAccount;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DaoITWithProdDb
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountDtoSpringJdbcDaoIT {

    public static final String SPLIT_REGEX = "\\s+";

    private final BankAccountDtoDao bankAccountDtoDao;
    private final BankAccountDao bankAccountDao;

    public BankAccountDtoSpringJdbcDaoIT(BankAccountDtoDao bankAccountDtoDao,
                                         BankAccountDao bankAccountDao) {
        this.bankAccountDtoDao = bankAccountDtoDao;
        this.bankAccountDao = bankAccountDao;
    }

    @Test
    void getAllWithTotalCards() {
        List<BankAccountDto> accounts = bankAccountDtoDao.getAllWithTotalCards();
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
        BankAccountsFilterDto bankAccountsFilterDto = new BankAccountsFilterDto();
        String numberPattern1 = "S8416E1PX";
        String customerPattern1 = "Iv";
        bankAccountsFilterDto.setNumberPattern(numberPattern1);
        bankAccountsFilterDto.setCustomerPattern(customerPattern1);
        List<BankAccountDto> accounts = bankAccountDtoDao.getAllWithTotalCards(bankAccountsFilterDto);
        assertEquals(accounts.size(), 1);
        assertTrue(accounts.get(0).getNumber().contains(numberPattern1));
        assertTrue(accounts.get(0).getCustomer().contains(customerPattern1));
        //Case 2
        String customerPattern2 = "ov";
        bankAccountsFilterDto.setNumberPattern(null);
        bankAccountsFilterDto.setCustomerPattern(customerPattern2);
        accounts = bankAccountDtoDao.getAllWithTotalCards(bankAccountsFilterDto);
        assertEquals(accounts.size(), 2);
        assertTrue(accounts.stream().allMatch(acc -> acc.getCustomer().contains(customerPattern2)));
        //Case 3
        String numberPattern2 = "BY 99T6";
        String customerPattern3 = "Iv ov";
        bankAccountsFilterDto.setNumberPattern(numberPattern2);
        bankAccountsFilterDto.setCustomerPattern(customerPattern3);
        accounts = bankAccountDtoDao.getAllWithTotalCards(bankAccountsFilterDto);
        assertEquals(accounts.size(), 1);
        String[] numberExpressions =  numberPattern2.split(SPLIT_REGEX);
        String number = accounts.get(0).getNumber();
        String[] customerExpressions = customerPattern3.split(SPLIT_REGEX);
        String customer = accounts.get(0).getCustomer();
        assertTrue(number.contains(numberExpressions[0]) && number.contains(numberExpressions[1]));
        assertTrue(customer.contains(customerExpressions[0]) && customer.contains(customerExpressions[1]));
        //case 4
        String numberPattern3 = "BY";
        bankAccountsFilterDto.setNumberPattern(numberPattern3);
        bankAccountsFilterDto.setCustomerPattern(null);
        accounts = bankAccountDtoDao.getAllWithTotalCards(bankAccountsFilterDto);
        assertEquals(accounts.size(), 3);
        assertTrue(accounts.stream().allMatch(acc -> acc.getNumber().contains(numberPattern3)));
        //case 5
        String customerPattern4 = "Customer";
        bankAccountsFilterDto.setNumberPattern(null);
        bankAccountsFilterDto.setCustomerPattern(customerPattern4);
        accounts = bankAccountDtoDao.getAllWithTotalCards(bankAccountsFilterDto);
        assertEquals(accounts.size(), 0);
    }

}