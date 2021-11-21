package com.epam.brest.impl;

import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountServiceImplIT extends BasicServiceIT {

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
    void update() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        bankAccount.setCustomer("Customer");
        Integer result = bankAccountService.update(bankAccount);
        assertEquals(result, 1);
    }

    @Test
    void deleteSucceeded() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCustomer("Customer");
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        Integer result = bankAccountService.delete(createdBankAccount);
        assertEquals(result, 1);
    }

    @Test
    void deleteFailed() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1);
        assertThrows(IllegalArgumentException.class, () -> bankAccountService.delete(bankAccount));
        BankAccount newBankAccountFromDb = bankAccountService.getById(bankAccount.getId());
        assertEquals(newBankAccountFromDb.getId(), 1);
    }

}