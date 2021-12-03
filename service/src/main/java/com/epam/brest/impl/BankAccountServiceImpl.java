package com.epam.brest.impl;

import com.epam.brest.dao.BankAccountDao;
import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.entity.BankAccount;
import com.epam.brest.service.BankAccountService;
import com.epam.brest.util.ServiceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.epam.brest.constant.ServiceConstant.JOIN_DELIMITER;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountServiceImpl.class);

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
        LOGGER.debug("getById(id={})", id);
        return bankAccountDao.getById(id)
                             .orElseThrow(() -> {
                                String error = String.format(findByIdError, id);
                                LOGGER.warn("getById(error={})", error);
                                return new IllegalArgumentException(error);
                             });
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        bankAccount.setNumber(getIban());
        bankAccount.setRegistrationDate(LocalDate.now());
        LOGGER.debug("create(bankAccount={})", bankAccount);
        return bankAccountDao.create(bankAccount);
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        LOGGER.debug("update(bankAccount={})", bankAccount);
        BankAccount bankAccountFromDb = getById(bankAccount.getId());
        LOGGER.info("update(bankAccountFromDb={})", bankAccountFromDb);
        ServiceUtils.copyProperties(bankAccount, bankAccountFromDb);
        LOGGER.debug("update(updatedBankAccount={})", bankAccountFromDb);
        bankAccountDao.update(bankAccountFromDb);
        return bankAccountFromDb;
    }

    @Override
    public BankAccount delete(Integer id) {
        LOGGER.debug("delete(id={})", id);
        BankAccount bankAccountFromDb = getById(id);
        List<String> linkedCards = bankAccountDao.getLinkedCards(bankAccountFromDb.getId());
        LOGGER.debug("delete(bankAccountFromDb={})", bankAccountFromDb);
        if (!linkedCards.isEmpty()) {
            String error = String.format(deleteError, bankAccountFromDb.getNumber(), String.join(JOIN_DELIMITER, linkedCards));
            LOGGER.warn("delete(error={})", error);
            throw new IllegalArgumentException(error);
        }
        bankAccountDao.delete(bankAccountFromDb.getId());
        return bankAccountFromDb;
    }

    private String getIban() {
        String iban;
        do {
            iban = bankDataGenerator.generateIban();
            LOGGER.info("generatedIban={}", iban);
        } while (bankAccountDao.isAccountNumberExists(iban));
        return iban;
    }

}
