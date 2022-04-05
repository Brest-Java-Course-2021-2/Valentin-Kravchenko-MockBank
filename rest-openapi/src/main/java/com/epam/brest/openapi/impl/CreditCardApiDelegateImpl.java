package com.epam.brest.openapi.impl;

import com.epam.brest.model.CreditCard;
import com.epam.brest.model.CreditCardTransactionDto;
import com.epam.brest.model.TransferTransactionDto;
import com.epam.brest.openapi.ApiDelegateBasic;
import com.epam.brest.openapi.api.CreditCardApiDelegate;
import com.epam.brest.service.api.CreditCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.Locale;

@Service
public class CreditCardApiDelegateImpl extends ApiDelegateBasic implements CreditCardApiDelegate {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardApiDelegateImpl.class);

    private final CreditCardService creditCardService;

    public CreditCardApiDelegateImpl(CreditCardService creditCardServiceImpl, Validator validator) {
        super(validator);
        this.creditCardService = creditCardServiceImpl;
    }

    @Override
    public ResponseEntity<CreditCard> transferMoney(TransferTransactionDto transferTransactionDto) {
        LOGGER.debug("transferTransactionDto={})", transferTransactionDto);
        CreditCardTransactionDto creditCardTransactionDto = new CreditCardTransactionDto();
        creditCardTransactionDto.setSourceCardNumber(transferTransactionDto.getSourceCardNumber());
        creditCardTransactionDto.setTargetCardNumber(transferTransactionDto.getTargetCardNumber());
        creditCardTransactionDto.setValueSumOfMoney(transferTransactionDto.getValueSumOfMoney());
        creditCardTransactionDto.setLocale(new Locale(transferTransactionDto.getLocale()));
        validate(creditCardTransactionDto);
        CreditCard sourceCreditCard = creditCardService.transfer(creditCardTransactionDto);
        return ResponseEntity.ok(sourceCreditCard);
    }

}
