package com.epam.brest.webapp.validator;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.CreditCardTransactionDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Objects;

import static com.epam.brest.webapp.constant.ControllerConstant.*;

@Component
public class CreditCardTransactionDtoValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardTransactionDtoValidator.class);

    private final BankDataGenerator bankDataGenerator;
    private final NumberStyleFormatter numberStyleFormatter;

    @Value("${card.number.regexp}")
    private String cardNumberRegexp;

    @Value("${card.transaction.amount.regexp}")
    private String sumOfMoneyRegexp;

    public CreditCardTransactionDtoValidator(BankDataGenerator bankDataGenerator,
                                             NumberStyleFormatter numberStyleFormatter) {
        this.bankDataGenerator = bankDataGenerator;
        this.numberStyleFormatter = numberStyleFormatter;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreditCardTransactionDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.debug("validate(target={})", target);
        CreditCardTransactionDto creditCardTransactionDto = (CreditCardTransactionDto) target;
        String targetCardNumber = creditCardTransactionDto.getTargetCardNumber();
        String sourceCardNumber = creditCardTransactionDto.getSourceCardNumber();
        validateCardNumber(targetCardNumber, errors, TARGET_CARD_NUMBER, ERROR_CODE_TARGET_CARD_NUMBER);
        if (Objects.nonNull(sourceCardNumber)) {
            validateCardNumber(sourceCardNumber, errors, SOURCE_CARD_NUMBER, ERROR_CODE_SOURCE_CARD_NUMBER);
            if (Objects.equals(targetCardNumber, sourceCardNumber)) {
                errors.rejectValue(TARGET_CARD_NUMBER, ERROR_CODE_DIFFERENT_CARD_NUMBERS);
            }
        }
        validateSumOfMoney(creditCardTransactionDto, errors);
    }

    private void validateSumOfMoney(CreditCardTransactionDto creditCardTransactionDto, Errors errors) {
        LOGGER.info("validateSumOfMoney(valueSumOfMoney={})", creditCardTransactionDto.getTransactionAmountValue());
        if(!creditCardTransactionDto.getTransactionAmountValue().matches(sumOfMoneyRegexp)) {
            errors.rejectValue(VALUE_SUM_OF_MONEY, ERROR_CODE_SUM_OF_MONEY);
        } else {
            try {
               numberStyleFormatter.parse(creditCardTransactionDto.getTransactionAmountValue(), creditCardTransactionDto.getLocale());
            } catch (ParseException e) {
                errors.rejectValue(VALUE_SUM_OF_MONEY, ERROR_CODE_SUM_OF_MONEY);
            }
        }
    }

    private void validateCardNumber(String cardNumber, Errors errors, String field, String errorCode) {
        LOGGER.info("validateCardNumber(cardNumber={})", cardNumber);
        if(!cardNumber.matches(cardNumberRegexp) || !bankDataGenerator.isCardNumberValid(cardNumber)) {
            errors.rejectValue(field, errorCode);
        }
    }

}

