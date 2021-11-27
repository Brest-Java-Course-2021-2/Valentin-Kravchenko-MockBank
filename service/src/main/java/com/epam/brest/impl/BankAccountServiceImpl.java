package com.epam.brest.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import com.epam.brest.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.epam.brest.constant.ServiceConstant.JOIN_DELIMITER;

@Service
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
        BankAccount bankAccountFromDb = getById(bankAccount.getId());
        ServiceUtils.copyProperties(bankAccount, bankAccountFromDb);
        return bankAccountDao.update(bankAccountFromDb);
    }

    @Override
    public Integer delete(BankAccount bankAccount) {
        BankAccount bankAccountFromDb = getById(bankAccount.getId());
        List<String> linkedCards = bankAccountDao.getLinkedCards(bankAccountFromDb);
        if (!linkedCards.isEmpty()) {
            throw new IllegalArgumentException(String.format(deleteError, bankAccount.getNumber(),
                                               String.join(JOIN_DELIMITER, linkedCards)));
        }
        return bankAccountDao.delete(bankAccountFromDb);
    }

    private String getIban() {
        String iban;
        do {
            iban = bankDataGenerator.generateIban();
        } while (bankAccountDao.isAccountNumberExists(iban));
        return iban;
    }

}
