package com.epam.brest.service.impl;

import com.epam.brest.dao.api.CreditCardDao;
import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.CreditCardTransactionDto;
import com.epam.brest.model.CreditCard;
import com.epam.brest.service.api.ExtendedCreditCardService;
import com.epam.brest.service.exception.CreditCardException;
import com.epam.brest.service.exception.ResourceNotFoundException;
import com.epam.brest.service.util.ServiceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;

import static com.epam.brest.service.constant.ServiceConstant.INIT_BALANCE;

@Service
@Transactional(readOnly = true)
public class CreditCardServiceImpl implements ExtendedCreditCardService {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardServiceImpl.class);

    private final CreditCardDao creditCardDao;
    private final BankDataGenerator bankDataGenerator;
    private final NumberStyleFormatter numberStyleFormatter;

    @Value("${card.error.find.by.id}")
    private String findByIdError;

    @Value("${card.error.find.by.number}")
    private String findByNumberError;

    @Value("${card.error.delete}")
    private String deleteError;

    @Value("${card.error.transfer}")
    private String transferError;

    public CreditCardServiceImpl(CreditCardDao creditCardDao,
                                 BankDataGenerator bankDataGenerator,
                                 NumberStyleFormatter numberStyleFormatter) {
        this.creditCardDao = creditCardDao;
        this.bankDataGenerator = bankDataGenerator;
        this.numberStyleFormatter = numberStyleFormatter;
    }

    @Override
    public CreditCard getById(Integer id) {
        LOGGER.debug("getCreditCardById(id={})", id);
        return creditCardDao.getById(id)
                            .orElseThrow(() -> {
                                String error = String.format(findByIdError, id);
                                LOGGER.warn("getCreditCardById(error={})", error);
                                return new ResourceNotFoundException(error);
                            });
    }

    @Override
    public CreditCard getByNumber(String cardNumber) {
        LOGGER.debug("getByNumber(cardNumber={})", cardNumber);
        return creditCardDao.getByNumber(cardNumber)
                            .orElseThrow(() -> {
                                String error = String.format(findByNumberError, cardNumber);
                                LOGGER.warn("getByNumber(error={})", error);
                                return new ResourceNotFoundException(error);
                            });
    }

    @Transactional
    @Override
    public CreditCard create(Integer accountId) {
        LOGGER.debug("create(accountId={})", accountId);
        CreditCard createdCreditCard = new CreditCard();
        createdCreditCard.setNumber(getCardNumber());
        createdCreditCard.setExpirationDate(ServiceUtils.convertToExpirationDate(LocalDate.now()));
        createdCreditCard.setBalance(new BigDecimal(INIT_BALANCE));
        createdCreditCard.setAccountId(accountId);
        LOGGER.debug("create(createdCreditCard={})", createdCreditCard);
        return creditCardDao.create(createdCreditCard);
    }

    @Transactional
    @Override
    public CreditCard delete(Integer id) {
        LOGGER.debug("delete(id={})", id);
        CreditCard deletedCardFromDb = getById(id);
        LOGGER.debug("delete(deletedCardFromDb={})", deletedCardFromDb);
        if (deletedCardFromDb.getBalance().signum() == 1) {
            String error = String.format(deleteError, deletedCardFromDb.getNumber(), deletedCardFromDb.getBalance().toPlainString());
            LOGGER.warn("delete(error={})", error);
            throw new CreditCardException(error);
        }
        creditCardDao.delete(deletedCardFromDb.getId());
        return deletedCardFromDb;
    }

    @Transactional
    @Override
    public CreditCard deposit(CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("deposit(creditCardTransactionDto={})", creditCardTransactionDto);
        CreditCard targetCreditCard = getByNumber(creditCardTransactionDto.getTargetCardNumber());
        LOGGER.debug("deposit(targetCreditCard={})", targetCreditCard);
        BigDecimal transactionAmount = getTransactionAmount(creditCardTransactionDto);
        BigDecimal newTargetCreditCardBalance = targetCreditCard.getBalance().add(transactionAmount);
        targetCreditCard.setBalance(newTargetCreditCardBalance);
        creditCardDao.update(targetCreditCard);
        return targetCreditCard;
    }

    @Transactional
    @Override
    public CreditCard transfer(CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("deposit(creditCardTransactionDto={})", creditCardTransactionDto);
        CreditCard sourceCreditCard = getByNumber(creditCardTransactionDto.getSourceCardNumber());
        LOGGER.debug("deposit(sourceCreditCard={})", sourceCreditCard);
        BigDecimal transactionAmount = getTransactionAmount(creditCardTransactionDto);
        if (sourceCreditCard.getBalance().compareTo(transactionAmount) < 0) {
            String error = String.format(transferError, creditCardTransactionDto.getSourceCardNumber());
            LOGGER.warn("deposit(error={})", error);
            throw new CreditCardException(error);
        }
        CreditCard targetCreditCard = getByNumber(creditCardTransactionDto.getTargetCardNumber());
        LOGGER.debug("deposit(targetCreditCard={})", targetCreditCard);
        BigDecimal newSourceCreditCardBalance = sourceCreditCard.getBalance().subtract(transactionAmount);
        BigDecimal newTargetCreditCardBalance = targetCreditCard.getBalance().add(transactionAmount);
        sourceCreditCard.setBalance(newSourceCreditCardBalance);
        targetCreditCard.setBalance(newTargetCreditCardBalance);
        creditCardDao.update(sourceCreditCard);
        creditCardDao.update(targetCreditCard);
        return sourceCreditCard;
    }

    private BigDecimal getTransactionAmount(CreditCardTransactionDto creditCardTransactionDto) {
        try {
            return (BigDecimal) numberStyleFormatter.parse(creditCardTransactionDto.getTransactionAmountValue(), creditCardTransactionDto.getLocale());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCardNumber() {
        LOGGER.info("getCardNumber()");
        String cardNumber;
        do {
            cardNumber = bankDataGenerator.generateCardNumber();
            LOGGER.info("generatedCardNumber={}", cardNumber);
        } while (creditCardDao.isCardNumberExists(cardNumber));
        return cardNumber;
    }

}
