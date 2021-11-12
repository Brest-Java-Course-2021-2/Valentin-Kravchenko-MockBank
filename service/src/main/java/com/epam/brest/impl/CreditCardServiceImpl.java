package com.epam.brest.impl;

import com.epam.brest.dao.CreditCardDao;
import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import com.epam.brest.util.ServiceUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    private static final String INIT_BALANCE = "0.00";

    private final CreditCardDao creditCardDao;
    private final BankDataGenerator bankDataGenerator;

    public CreditCardServiceImpl(CreditCardDao creditCardDao, BankDataGenerator bankDataGenerator) {
        this.creditCardDao = creditCardDao;
        this.bankDataGenerator = bankDataGenerator;
    }

    @Override
    public CreditCard create(Integer bankAccountId) {
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber(getCardNumber());
        creditCard.setExpirationDate(ServiceUtils.convertToExpirationDate(LocalDate.now(ZoneId.systemDefault())));
        creditCard.setBalance(new BigDecimal(INIT_BALANCE));
        creditCard.setAccountId(bankAccountId);
        return creditCardDao.create(creditCard);
    }

    @Override
    public Integer delete(CreditCard creditCard) {
        return creditCardDao.delete(creditCard);
    }

    private String getCardNumber() {
        String cardNumber;
        do {
            cardNumber = bankDataGenerator.generateCardNumber();
        } while (creditCardDao.isCardNumberExists(cardNumber));
        return cardNumber;
    }

}
