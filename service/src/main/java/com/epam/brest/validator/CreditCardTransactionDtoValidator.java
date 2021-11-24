package com.epam.brest.validator;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.dto.CreditCardTransactionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.Objects;

import static com.epam.brest.constant.ServiceConstant.*;

public class CreditCardTransactionDtoValidator implements Validator {

    private final BankDataGenerator bankDataGenerator;

    @Value("${card.number.regexp}")
    private String numberRegexp;

    @Value("${card.money.integer}")
    private String integerValue;

    @Value("${card.money.fraction}")
    private String fractionValue;

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
        BigDecimal sumOfMoney = creditCardTransactionDto.getSumOfMoney();
        if (sumOfMoney.signum() <= 0) {
            errors.rejectValue(SUM_OF_MONEY, ERROR_CODE_SUM_OF_MONEY_POSITIVE);
        } else {
            int integerPart = Integer.parseInt(integerValue);
            int fractionalPart = Integer.parseInt(fractionValue);
            if ((sumOfMoney.precision() - sumOfMoney.scale()) > integerPart || sumOfMoney.scale() > fractionalPart) {
                errors.rejectValue(SUM_OF_MONEY, ERROR_CODE_SUM_OF_MONEY);
            }
        }
    }

    private void validateCardNumber(String cardNumber, Errors errors, String field, String errorCode) {
        if(!cardNumber.matches(numberRegexp) || !bankDataGenerator.isCardNumberValid(cardNumber)) {
            errors.rejectValue(field, errorCode);
        }
    }

}

