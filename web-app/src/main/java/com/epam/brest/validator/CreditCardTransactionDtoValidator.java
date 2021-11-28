package com.epam.brest.validator;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.dto.CreditCardTransactionDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Objects;

import static com.epam.brest.constant.ControllerConstant.*;

@Component
public class CreditCardTransactionDtoValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardTransactionDtoValidator.class);

    private final BankDataGenerator bankDataGenerator;

    @Value("${card.number.regexp}")
    private String numberRegexp;

    @Value("${card.sum.money.regexp}")
    private String sumOfMoneyRegexp;

    public CreditCardTransactionDtoValidator(BankDataGenerator bankDataGenerator) {
        this.bankDataGenerator = bankDataGenerator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreditCardTransactionDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreditCardTransactionDto creditCardTransactionDto = (CreditCardTransactionDto) target;
        LOGGER.debug("validate(creditCardTransactionDto={})", creditCardTransactionDto);
        String sourceCardNumber = creditCardTransactionDto.getSourceCardNumber();
        String targetCardNumber = creditCardTransactionDto.getTargetCardNumber();
        if (Objects.nonNull(sourceCardNumber)) {
            validateCardNumber(sourceCardNumber, errors, SOURCE_CARD_NUMBER, ERROR_CODE_SOURCE_CARD_NUMBER);
            if (Objects.equals(targetCardNumber, sourceCardNumber)) {
                errors.rejectValue(TARGET_CARD_NUMBER, ERROR_CODE_TARGET_NUMBER_DIFFERENT);
            }
        }
        validateCardNumber(targetCardNumber, errors, TARGET_CARD_NUMBER, ERROR_CODE_TARGET_CARD_NUMBER);
        validateSumOfMoney(creditCardTransactionDto, errors);
    }

    private void validateSumOfMoney(CreditCardTransactionDto creditCardTransactionDto, Errors errors) {
        String sumOfMoney = creditCardTransactionDto.getSumOfMoney();
        LOGGER.info("validateSumOfMoney(sumOfMoney={})", sumOfMoney);
        if(!sumOfMoney.matches(sumOfMoneyRegexp)) {
            errors.rejectValue(SUM_OF_MONEY, ERROR_CODE_SUM_OF_MONEY);
        } else {
            NumberFormat numberFormat = NumberFormat.getInstance(creditCardTransactionDto.getLocale());
            ParsePosition parsePosition = new ParsePosition(0);
            numberFormat.parse(sumOfMoney, parsePosition);
            if(sumOfMoney.length() != parsePosition.getIndex()) {
                errors.rejectValue(SUM_OF_MONEY, ERROR_CODE_SUM_OF_MONEY);
            }
        }
    }

    private void validateCardNumber(String cardNumber, Errors errors, String field, String errorCode) {
        LOGGER.info("validateCardNumber(cardNumber={})", cardNumber);
        if(!cardNumber.matches(numberRegexp) || !bankDataGenerator.isCardNumberValid(cardNumber)) {
            errors.rejectValue(field, errorCode);
        }
    }

}

