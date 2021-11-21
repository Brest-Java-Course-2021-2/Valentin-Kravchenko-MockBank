package com.epam.brest.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.epam.brest.constant.ServiceConstant.DELIMITER;

@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountDao bankAccountDao;
    private final BankDataGenerator bankDataGenerator;

    @Value("${account.error.find.by.id}")
    private String findByIdError;

    @Value("${account.error.delete}")
    private String deleteError;

    public BankAccountServiceImpl(BankAccountDao bankAccountDao, BankDataGenerator bankDataGenerator) {
        this.bankAccountDao = bankAccountDao;
        this.bankDataGenerator = bankDataGenerator;
    }

    @Override
    public BankAccount getById(Integer id) {
        return bankAccountDao.getById(id).orElseThrow(() -> new IllegalArgumentException(String.format(findByIdError, id)));
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        bankAccount.setNumber(getIban());
        bankAccount.setRegistrationDate(LocalDate.now());
        return bankAccountDao.create(bankAccount);
    }

    @Override
    public Integer update(BankAccount bankAccount) {
        return bankAccountDao.update(bankAccount);
    }

    @Override
    public Integer delete(BankAccount bankAccount) {
        List<String> linkedCards = bankAccountDao.getLinkedCards(bankAccount);
        if (!linkedCards.isEmpty()) {
            throw new IllegalArgumentException(String.format(deleteError, bankAccount.getNumber(),
                                               String.join(DELIMITER, linkedCards)));
        }
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
