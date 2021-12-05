package com.epam.brest.validator;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.dto.CreditCardTransactionDto;
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

import static com.epam.brest.constant.ControllerConstant.*;

@Component
public class CreditCardTransactionDtoValidator implements Validator {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardTransactionDtoValidator.class);

    private final BankDataGenerator bankDataGenerator;
    private final NumberStyleFormatter numberStyleFormatter;

    @Value("${card.number.regexp}")
    private String numberRegexp;

    @Value("${card.sum.money.regexp}")
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
        String sourceCardNumber = creditCardTransactionDto.getSourceCardNumber();
        String targetCardNumber = creditCardTransactionDto.getTargetCardNumber();
        if (Objects.nonNull(sourceCardNumber)) {
            validateCardNumber(sourceCardNumber, errors, SOURCE_CARD_NUMBER, ERROR_CODE_CARD_NUMBER_SOURCE_NUMBER);
            if (Objects.equals(targetCardNumber, sourceCardNumber)) {
                errors.rejectValue(TARGET_CARD_NUMBER, ERROR_CODE_NON_DUPLICATE_TARGET_CARD_NUMBER);
            }
        }
        validateCardNumber(targetCardNumber, errors, TARGET_CARD_NUMBER, ERROR_CODE_CARD_NUMBER_TARGET_NUMBER);
        validateSumOfMoney(creditCardTransactionDto, errors);
    }

    private void validateSumOfMoney(CreditCardTransactionDto creditCardTransactionDto, Errors errors) {
        LOGGER.info("validateSumOfMoney(valueSumOfMoney={})", creditCardTransactionDto.getValueSumOfMoney());
        if(!creditCardTransactionDto.getValueSumOfMoney().matches(sumOfMoneyRegexp)) {
            errors.rejectValue(VALUE_SUM_OF_MONEY, ERROR_CODE_SUM_OF_MONEY);
        } else {
            try {
                BigDecimal sumOfMoney = (BigDecimal) numberStyleFormatter.parse(creditCardTransactionDto.getValueSumOfMoney(),
                                                                                creditCardTransactionDto.getLocale());
                creditCardTransactionDto.setSumOfMoney(sumOfMoney);
            } catch (ParseException e) {
                errors.rejectValue(VALUE_SUM_OF_MONEY, ERROR_CODE_SUM_OF_MONEY);
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

