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

import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;

import static com.epam.brest.webapp.constant.ControllerConstant.*;
import static java.util.Objects.nonNull;

@Component
public class CreditCardTransactionDtoValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardTransactionDtoValidator.class);

    private final BankDataGenerator bankDataGenerator;
    private final NumberStyleFormatter numberStyleFormatter;

    @Value("${card.number.regexp}")
    private String cardNumberRegexp;

    @Value("${card.transaction.amount.regexp}")
    private String transactionAmountRegexp;

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
        if (!isCardNumberValid(targetCardNumber)) {
            errors.rejectValue(TARGET_CARD_NUMBER, TARGET_CARD_NUMBER_ERROR_CODE);
        }
        if (nonNull(sourceCardNumber)) {
            if (!isCardNumberValid(sourceCardNumber)) {
                errors.rejectValue(SOURCE_CARD_NUMBER, SOURCE_CARD_NUMBER_ERROR_CODE);
            }
            if (Objects.equals(sourceCardNumber, targetCardNumber)) {
                errors.rejectValue(TARGET_CARD_NUMBER, DIFFERENT_CARD_NUMBERS_ERROR_CODE);
            }
        }
        if (!isTransactionAmountValid(creditCardTransactionDto.getTransactionAmountValue(), creditCardTransactionDto.getLocale())) {
            errors.rejectValue(TRANSACTION_AMOUNT, TRANSACTION_AMOUNT_ERROR_CODE);
        }
    }

    private boolean isTransactionAmountValid(String transactionAmountValue, Locale locale) {
        LOGGER.trace("isTransactionAmountValid(transactionAmountValue={}, locale={})", transactionAmountValue, locale);
        if (!transactionAmountValue.matches(transactionAmountRegexp)) {
            return false;
        } else {
            try {
                numberStyleFormatter.parse(transactionAmountValue, locale);
            } catch (ParseException e) {
                return false;
            }
        }
        return true;
    }

    private boolean isCardNumberValid(String cardNumberValue) {
        LOGGER.trace("isCardNumberValid(cardNumberValue={})", cardNumberValue);
        return cardNumberValue.matches(cardNumberRegexp) && bankDataGenerator.isCardNumberValid(cardNumberValue);
    }

}

