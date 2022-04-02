package com.epam.brest.service.impl;

import com.epam.brest.dao.api.BankAccountDao;
import com.epam.brest.dao.api.CreditCardDao;
import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.BankAccount;
import com.epam.brest.model.CreditCard;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.exception.BankAccountException;
import com.epam.brest.service.exception.ResourceNotFoundException;
import com.epam.brest.service.util.ServiceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.brest.service.constant.ServiceConstant.JOIN_DELIMITER;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountServiceImpl.class);

    private final BankAccountDao bankAccountDao;
    private final CreditCardDao creditCardDao;
    private final BankDataGenerator bankDataGenerator;

    @Value("${account.error.find.by.id}")
    private String findByIdError;

    @Value("${account.error.delete}")
    private String deleteError;

    public BankAccountServiceImpl(BankAccountDao bankAccountDao,
                                  CreditCardDao creditCardDao,
                                  BankDataGenerator bankDataGenerator) {
        this.bankAccountDao = bankAccountDao;
        this.creditCardDao = creditCardDao;
        this.bankDataGenerator = bankDataGenerator;
    }

    @Override
    public BankAccount getById(Integer id) {
        LOGGER.debug("getBankAccountById(id={})", id);
        return bankAccountDao.getById(id)
                             .orElseThrow(() -> {
                                String error = String.format(findByIdError, id);
                                LOGGER.warn("getBankAccountById(error={})", error);
                                return new ResourceNotFoundException(error);
                             });
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        LOGGER.debug("create(bankAccount={})", bankAccount);
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
        List<CreditCard> linkedCards = getAllCardsById(bankAccountFromDb.getId());
        LOGGER.debug("delete(bankAccountFromDb={})", bankAccountFromDb);
        if (!linkedCards.isEmpty()) {
            List<String> linkedCardNumbers = linkedCards.stream()
                                                        .map(CreditCard::getNumber)
                                                        .collect(Collectors.toList());
            String error = String.format(deleteError, bankAccountFromDb.getNumber(), String.join(JOIN_DELIMITER, linkedCardNumbers));
            LOGGER.warn("delete(error={})", error);
            throw new BankAccountException(error);
        }
        bankAccountDao.delete(bankAccountFromDb.getId());
        return bankAccountFromDb;
    }

    @Override
    public List<CreditCard> getAllCardsById(Integer id) {
        LOGGER.debug("getAllByAccountId(accountId={})", id);
        BankAccount bankAccountFromDb = getById(id);
        LOGGER.debug("getAllCardsById(bankAccountFromDb={})", bankAccountFromDb);
        return creditCardDao.getAllByAccountId(bankAccountFromDb.getId());
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
