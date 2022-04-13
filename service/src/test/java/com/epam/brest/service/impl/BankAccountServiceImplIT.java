package com.epam.brest.service.impl;

import com.epam.brest.model.BankAccount;
import com.epam.brest.model.CreditCard;
import com.epam.brest.service.annotation.ServiceIT;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.exception.BankAccountException;
import com.epam.brest.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ServiceIT
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BankAccountServiceImplIT {

    public static final String NEW_CUSTOMER = "New Customer";

    private final BankAccountService bankAccountService;

    public BankAccountServiceImplIT(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Test
    void create() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer(NEW_CUSTOMER);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        assertNotNull(createdBankAccount.getId());
        assertNotNull(createdBankAccount.getNumber());
        assertEquals(bankAccount.getCustomer(), createdBankAccount.getCustomer());
        assertEquals(LocalDate.now(), createdBankAccount.getRegistrationDate());
    }

    @Test
    void update() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        bankAccount.setCustomer(NEW_CUSTOMER);
        BankAccount updatedBankAccount = bankAccountService.update(bankAccount);
        assertEquals(updatedBankAccount.getCustomer(), NEW_CUSTOMER);
    }

    @Test
    void updateFail() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1000);
        bankAccount.setCustomer(NEW_CUSTOMER);
        assertThrows(ResourceNotFoundException.class, () -> bankAccountService.update(bankAccount));
    }

    @Test
    void remove() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer(NEW_CUSTOMER);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        BankAccount deletedBankAccount = bankAccountService.delete(createdBankAccount.getId());
        assertEquals(createdBankAccount, deletedBankAccount);
    }

    @Test
    void deleteFail() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        assertThrows(BankAccountException.class, () -> bankAccountService.delete(bankAccount.getId()));
        BankAccount newBankAccountFromDb = bankAccountService.getById(bankAccount.getId());
        assertEquals(newBankAccountFromDb.getId(), 1);
    }

    @Test
    void getById() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer(NEW_CUSTOMER);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        BankAccount bankAccountFromDb = bankAccountService.getById(createdBankAccount.getId());
        assertEquals(createdBankAccount, bankAccountFromDb);
    }

    @Test
    void getAllCardsById() {
        Integer id = 1;
        List<CreditCard> cards = bankAccountService.getAllCardsById(id);
        assertTrue(cards.stream().map(CreditCard::getAccountId).allMatch(accountId -> accountId.equals(id)));
    }

}