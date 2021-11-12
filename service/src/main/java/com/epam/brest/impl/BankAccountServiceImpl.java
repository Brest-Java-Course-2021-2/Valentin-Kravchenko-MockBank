package com.epam.brest.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountDao bankAccountDao;
    private final BankDataGenerator bankDataGenerator;

    public BankAccountServiceImpl(BankAccountDao bankAccountDao, BankDataGenerator bankDataGenerator) {
        this.bankAccountDao = bankAccountDao;
        this.bankDataGenerator = bankDataGenerator;
    }

    @Override
    public BankAccount create(String customer) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setNumber(getIban());
        bankAccount.setCustomer(customer);
        bankAccount.setRegistrationDate(LocalDate.now());
        return bankAccountDao.create(bankAccount);
    }

    @Override
    public Integer update(BankAccount bankAccount) {
        return bankAccountDao.update(bankAccount);
    }

    @Override
    public Integer delete(BankAccount bankAccount) {
        return bankAccountDao.delete(bankAccount);
    }

    private String getIban() {
        String iban;
        do {
            iban = bankDataGenerator.generateIban();
        } while (bankAccountDao.isAccountNumberExists(iban));
        return iban;
    }

}
