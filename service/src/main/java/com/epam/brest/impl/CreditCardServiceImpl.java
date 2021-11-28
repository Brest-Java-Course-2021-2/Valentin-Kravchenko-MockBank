package com.epam.brest.impl;

import com.epam.brest.dao.CreditCardDao;
import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import com.epam.brest.util.ServiceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.epam.brest.constant.ServiceConstant.INIT_BALANCE;

@Service
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardServiceImpl.class);

    private final CreditCardDao creditCardDao;
    private final BankDataGenerator bankDataGenerator;

    @Value("${card.error.find.by.id}")
    private String findByIdError;

    @Value("${card.error.find.by.number}")
    private String findByNumberError;

    @Value("${card.error.delete}")
    private String deleteError;

    @Value("${card.error.transfer}")
    private String transferError;

    public CreditCardServiceImpl(CreditCardDao creditCardDao, BankDataGenerator bankDataGenerator) {
        this.creditCardDao = creditCardDao;
        this.bankDataGenerator = bankDataGenerator;
    }

    @Override
    public CreditCard getById(Integer id) {
        LOGGER.debug("getById(id={})", id);
        return creditCardDao.getById(id)
                            .orElseThrow(() -> {
                                String error = String.format(findByIdError, id);
                                LOGGER.error("getById(error={})", error);
                                return new IllegalArgumentException(error);
                            });
    }

    @Override
    public CreditCard getByNumber(String cardNumber) {
        LOGGER.debug("getByNumber(cardNumber={})", cardNumber);
        return creditCardDao.getByNumber(cardNumber)
                            .orElseThrow(() -> {
                                String error = String.format(findByNumberError, cardNumber);
                                LOGGER.error("getByNumber(error={})", error);
                                return new IllegalArgumentException(error);
                            });
    }

    @Override
    public CreditCard create(Integer accountId) {
        LOGGER.debug("create(accountId={})", accountId);
        CreditCard newCreditCard = new CreditCard();
        newCreditCard.setNumber(getCardNumber());
        newCreditCard.setExpirationDate(ServiceUtils.convertToExpirationDate(LocalDate.now()));
        newCreditCard.setBalance(new BigDecimal(INIT_BALANCE));
        newCreditCard.setAccountId(accountId);
        LOGGER.debug("create(newCreditCard={})", newCreditCard);
        return creditCardDao.create(newCreditCard);
    }

    @Override
    public CreditCard delete(Integer id) {
        LOGGER.debug("delete(id={})", id);
        CreditCard creditCardFromDb = getById(id);
        LOGGER.debug("delete(creditCardFromDb={})", creditCardFromDb);
        if (creditCardFromDb.getBalance().signum() == 1) {
            String error = String.format(deleteError, creditCardFromDb.getNumber(), creditCardFromDb.getBalance().toString());
            LOGGER.error("delete(error={})", error);
            throw new IllegalArgumentException(error);
        }
        creditCardDao.delete(creditCardFromDb.getId());
        return creditCardFromDb;
    }

    @Override
    public boolean deposit(CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("deposit(creditCardTransactionDto={})", creditCardTransactionDto);
        CreditCard targetCreditCard = getByNumber(creditCardTransactionDto.getTargetCardNumber());
        LOGGER.debug("deposit(targetCreditCard={})", targetCreditCard);
        BigDecimal sumOfMoney = getSumOfMoney(creditCardTransactionDto.getSumOfMoney(), creditCardTransactionDto.getLocale());
        BigDecimal newBalance = targetCreditCard.getBalance().add(sumOfMoney);
        LOGGER.debug("deposit(newBalance={})", newBalance);
        targetCreditCard.setBalance(newBalance);
        return creditCardDao.update(targetCreditCard) == 1;
    }

    @Override
    public boolean transfer(CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("deposit(creditCardTransactionDto={})", creditCardTransactionDto);
        CreditCard sourceCreditCard = getByNumber(creditCardTransactionDto.getSourceCardNumber());
        LOGGER.debug("deposit(sourceCreditCard={})", sourceCreditCard);
        BigDecimal sumOfMoney = getSumOfMoney(creditCardTransactionDto.getSumOfMoney(), creditCardTransactionDto.getLocale());
        if (sourceCreditCard.getBalance().compareTo(sumOfMoney) < 0) {
            String error = String.format(transferError, creditCardTransactionDto.getSourceCardNumber());
            LOGGER.error("deposit(error={})", error);
            throw new IllegalArgumentException(error);
        }
        CreditCard targetCreditCard = getByNumber(creditCardTransactionDto.getTargetCardNumber());
        LOGGER.debug("deposit(targetCreditCard={})", targetCreditCard);
        BigDecimal newSourceCreditCardBalance = sourceCreditCard.getBalance().subtract(sumOfMoney);
        LOGGER.debug("deposit(newSourceCreditCardBalance={})", newSourceCreditCardBalance);
        BigDecimal newTargetCreditCardBalance = targetCreditCard.getBalance().add(sumOfMoney);
        LOGGER.debug("deposit(newTargetCreditCardBalance={})", newTargetCreditCardBalance);
        sourceCreditCard.setBalance(newSourceCreditCardBalance);
        targetCreditCard.setBalance(newTargetCreditCardBalance);
        return creditCardDao.update(sourceCreditCard) == 1 && creditCardDao.update(targetCreditCard) == 1;
    }

    @Override
    public List<CreditCard> getAllByAccountId(Integer accountId) {
        LOGGER.debug("getAllByAccountId(accountId={})", accountId);
        return creditCardDao.getAllByAccountId(accountId);
    }

    private BigDecimal getSumOfMoney(String value, Locale locale) {
        LOGGER.debug("getSumOfMoney(value={}, locale={})", value, locale);
        NumberFormat decimalFormat = NumberFormat.getInstance(locale);
        try {
            return new BigDecimal(decimalFormat.parse(value).toString());
        } catch (ParseException e) {
            LOGGER.error("getSumOfMoney(error={})", e.getMessage());
            throw new RuntimeException();
        }
    }

    private String getCardNumber() {
        String cardNumber;
        do {
            cardNumber = bankDataGenerator.generateCardNumber();
            LOGGER.debug("generatedCardNumber={}", cardNumber);
        } while (creditCardDao.isCardNumberExists(cardNumber));
        return cardNumber;
    }

}
