package com.epam.brest.impl;

import com.epam.brest.BasicServiseTest;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BankAccountServiceImplIT extends BasicServiseTest {

    private final BankAccountService bankAccountService;

    public BankAccountServiceImplIT(@Autowired BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Test
    void create() {
        String customer = "Customer";
        BankAccount bankAccount = bankAccountService.create(customer);
        assertNotNull(bankAccount.getId());
        assertNotNull(bankAccount.getNumber());
        assertEquals(customer, bankAccount.getCustomer());
        assertEquals(LocalDate.now(), bankAccount.getRegistrationDate());
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
    void delete() {
        String customer = "Customer";
        BankAccount bankAccount = bankAccountService.create(customer);
        Integer result = bankAccountService.delete(bankAccount);
        assertEquals(result, 1);
    }

}