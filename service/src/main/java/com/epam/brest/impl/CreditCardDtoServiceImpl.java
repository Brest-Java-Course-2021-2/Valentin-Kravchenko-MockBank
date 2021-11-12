package com.epam.brest.impl;

import com.epam.brest.dao.CreditCardDao;
import com.epam.brest.dao.CreditCardDtoDao;
import com.epam.brest.model.dto.CreditCardDepositDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.model.dto.CreditCardTransferDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.CreditCardDtoService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Transactional
public class CreditCardDtoServiceImpl implements CreditCardDtoService {

    private final CreditCardDtoDao creditCardDtoDao;
    private final CreditCardDao creditCardDao;

    public CreditCardDtoServiceImpl(CreditCardDtoDao creditCardDtoDao, CreditCardDao creditCardDao) {
        this.creditCardDtoDao = creditCardDtoDao;
        this.creditCardDao = creditCardDao;
    }

    @Override
    public List<CreditCardDto> getAllWithAccountNumber() {
        return creditCardDtoDao.getAllWithAccountNumber();
    }

    @Override
    public boolean deposit(CreditCardDepositDto creditCardDepositDto) {
        CreditCard creditCard = getCreditCard(creditCardDepositDto.getCardNumber());
        BigDecimal newBalance = creditCard.getBalance().add(creditCardDepositDto.getSumOfMoney());
        creditCard.setBalance(newBalance);
        return creditCardDao.update(creditCard) == 1;
    }

    @Override
    public boolean transfer(CreditCardTransferDto creditCardTransferDto) {
        CreditCard sourceCreditCard = getCreditCard(creditCardTransferDto.getSourceCardNumber());
        if (sourceCreditCard.getBalance().compareTo(creditCardTransferDto.getSumOfMoney()) < 0) {
            return false;
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
        return optionalCreditCard.orElseThrow(() -> new IllegalArgumentException());
    }

}
