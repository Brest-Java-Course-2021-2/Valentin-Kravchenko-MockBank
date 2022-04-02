package com.epam.brest.service.util;

import com.epam.brest.model.BankAccount;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class ServiceUtilsTest {

    @Test
    void convertToExpirationDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = ServiceUtils.convertToExpirationDate(currentDate);
        assertEquals(expirationDate.getDayOfMonth(), YearMonth.now().atEndOfMonth().getDayOfMonth());
        assertEquals(currentDate.getMonth(), expirationDate.getMonth());
        int circulationInYears = 3;
        assertEquals(currentDate.getYear(), expirationDate.minusYears(circulationInYears).getYear());
    }

    @Test
    void copyProperties() {
        BankAccount bankAccountSource = new BankAccount();
        bankAccountSource.setId(1);
        bankAccountSource.setCustomer("New Customer");
        BankAccount bankAccountTarget= new BankAccount();
        bankAccountTarget.setId(1);
        bankAccountTarget.setCustomer("Old Customer");
        bankAccountTarget.setRegistrationDate(LocalDate.now());
        bankAccountTarget.setNumber("Number");
        ServiceUtils.copyProperties(bankAccountSource, bankAccountTarget);
        assertEquals(bankAccountTarget.getCustomer(), "New Customer");
        assertEquals(bankAccountTarget.getNumber(), "Number");
        assertEquals(bankAccountTarget.getRegistrationDate(), LocalDate.now());
    }
    
}