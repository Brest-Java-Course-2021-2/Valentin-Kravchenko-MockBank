package com.epam.brest.impl;

import com.epam.brest.dao.CreditCardDao;
import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.dto.CreditCardDepositDto;
import com.epam.brest.model.dto.CreditCardTransferDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardService;
import com.epam.brest.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    private static final String INIT_BALANCE = "0.00";

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
        Optional<CreditCard> optionalCreditCard = creditCardDao.getById(id);
        return optionalCreditCard.orElseThrow(() -> new IllegalArgumentException(String.format(findByIdError, id)));
    }

    @Override
    public CreditCard create(Integer bankAccountId) {
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber(getCardNumber());
        creditCard.setExpirationDate(ServiceUtils.convertToExpirationDate(LocalDate.now()));
        creditCard.setBalance(new BigDecimal(INIT_BALANCE));
        creditCard.setAccountId(bankAccountId);
        return creditCardDao.create(creditCard);
    }

    @Override
    public Integer delete(CreditCard creditCard) {
        CreditCard creditCardFromDb = getById(creditCard.getId());
        if (creditCardFromDb.getBalance().signum() == 1) {
            throw new IllegalArgumentException(String.format(deleteError, creditCardFromDb.getNumber(),
                                                             creditCardFromDb.getBalance().toString()));
        }
        return creditCardDao.delete(creditCardFromDb);
    }

    @Override
    public boolean deposit(CreditCardDepositDto creditCardDepositDto) {
        CreditCard creditCard = getCreditCard(creditCardDepositDto.getTargetCardNumber());
        BigDecimal newBalance = creditCard.getBalance().add(creditCardDepositDto.getSumOfMoney());
        creditCard.setBalance(newBalance);
        return creditCardDao.update(creditCard) == 1;
    }

    @Override
    public boolean transfer(CreditCardTransferDto creditCardTransferDto) {
        CreditCard sourceCreditCard = getCreditCard(creditCardTransferDto.getSourceCardNumber());
        if (sourceCreditCard.getBalance().compareTo(creditCardTransferDto.getSumOfMoney()) < 0) {
            throw new IllegalArgumentException(String.format(transferError, creditCardTransferDto.getSourceCardNumber()));
        }
        CreditCard targetCreditCard = getCreditCard(creditCardTransferDto.getTargetCardNumber());
        BigDecimal newBalanceSourceCreditCard = sourceCreditCard.getBalance().subtract(creditCardTransferDto.getSumOfMoney());
        BigDecimal newBalanceTargetCreditCard =targetCreditCard.getBalance().add(creditCardTransferDto.getSumOfMoney());
        sourceCreditCard.setBalance(newBalanceSourceCreditCard);
        targetCreditCard.setBalance(newBalanceTargetCreditCard);
        return creditCardDao.update(sourceCreditCard) == 1 && creditCardDao.update(targetCreditCard) == 1;
    }

    private CreditCard getCreditCard(String  cardNumber) {
        Optional<CreditCard> optionalCreditCard = creditCardDao.getByNumber(cardNumber);
        return optionalCreditCard.orElseThrow(() -> new IllegalArgumentException(String.format(findByNumberError, cardNumber)));
    }

    private String getCardNumber() {
        String cardNumber;
        do {
            cardNumber = bankDataGenerator.generateCardNumber();
        } while (creditCardDao.isCardNumberExists(cardNumber));
        return cardNumber;
    }

}
