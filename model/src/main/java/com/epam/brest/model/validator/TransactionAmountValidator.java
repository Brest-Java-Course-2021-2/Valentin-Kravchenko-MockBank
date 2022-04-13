package com.epam.brest.model.validator;

import com.epam.brest.model.CreditCardTransactionDto;
import com.epam.brest.model.validator.constraint.TransactionAmount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.number.NumberStyleFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;

import static com.epam.brest.model.validator.constant.ValidatorConstant.TRANSACTION_AMOUNT_TEMPLATE;
import static com.epam.brest.model.validator.constant.ValidatorConstant.TRANSACTION_AMOUNT_VALUE;

public class TransactionAmountValidator extends BasicValidator implements ConstraintValidator<TransactionAmount, CreditCardTransactionDto> {

    private static final Logger LOGGER = LogManager.getLogger(TransactionAmountValidator.class);

    private final NumberStyleFormatter numberStyleFormatter;

    @Value("${card.transaction.amount.regexp}")
    private String transactionAmountRegexp;

    public TransactionAmountValidator(NumberStyleFormatter numberStyleFormatter) {
        this.numberStyleFormatter = numberStyleFormatter;
    }

    @Override
    public boolean isValid(CreditCardTransactionDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if(!value.getTransactionAmountValue().matches(transactionAmountRegexp)) {
            buildConstraintViolation(context, TRANSACTION_AMOUNT_TEMPLATE, TRANSACTION_AMOUNT_VALUE);
            return false;
        }
        try {
            numberStyleFormatter.parse(value.getTransactionAmountValue(), value.getLocale());
        } catch (ParseException e) {
            buildConstraintViolation(context, TRANSACTION_AMOUNT_TEMPLATE, TRANSACTION_AMOUNT_VALUE);
            return false;
        }
        return true;
    }

}
