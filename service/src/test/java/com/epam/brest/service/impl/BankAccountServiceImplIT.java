package com.epam.brest.service.impl;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.exception.BankAccountException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceImplIT extends ServiceTestConfiguration {

    private final BankAccountService bankAccountService;

    public BankAccountServiceImplIT(@Autowired BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Test
    void create() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("Customer");
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        assertNotNull(createdBankAccount.getId());
        assertNotNull(createdBankAccount.getNumber());
        assertEquals(bankAccount.getCustomer(), createdBankAccount.getCustomer());
        assertEquals(LocalDate.now(), createdBankAccount.getRegistrationDate());
    }

    @Test
    void updateSucceeded() {
        BankAccount bankAccount = new BankAccount();
        String customer = "Customer";
        bankAccount.setId(1);
        bankAccount.setCustomer(customer);
        BankAccount updatedBankAccount = bankAccountService.update(bankAccount);
        assertEquals(updatedBankAccount.getCustomer(), customer);
    }

    @Test
    void updateFailed() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1000);
        bankAccount.setCustomer("Customer");
        assertThrows(BankAccountException.class, () -> bankAccountService.update(bankAccount));
    }

    @Test
    void deleteSucceeded() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("Customer");
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        BankAccount deletedBankAccount = bankAccountService.delete(createdBankAccount.getId());
        assertEquals(createdBankAccount, deletedBankAccount);
    }

    @Test
    void deleteFailed() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        assertThrows(BankAccountException.class, () -> bankAccountService.delete(bankAccount.getId()));
        BankAccount newBankAccountFromDb = bankAccountService.getById(bankAccount.getId());
        assertEquals(newBankAccountFromDb.getId(), 1);
    }

}