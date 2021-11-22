package com.epam.brest.validator;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.dto.CreditCardTransactionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

import static com.epam.brest.constant.ServiceConstant.*;

public class CreditCardTransactionDtoValidator implements Validator {

    private final BankDataGenerator bankDataGenerator;

    @Value("${card.number.regexp}")
    private String numberRegexp;

    @Value("${card.money.regexp}")
    private String moneyRegexp;

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
        if (Objects.nonNull(creditCardTransactionDto.getSourceCardNumber())) {
            if (Objects.equals(creditCardTransactionDto.getSourceCardNumber(), creditCardTransactionDto.getTargetCardNumber())) {
                errors.rejectValue(TARGET_CARD_NUMBER, ERROR_CODE_CARD_NUMBER);
            }
            validateCardNumber(creditCardTransactionDto.getSourceCardNumber(), errors, SOURCE_CARD_NUMBER, ERROR_CODE_SOURCE_CARD_NUMBER);
        }
        validateCardNumber(creditCardTransactionDto.getTargetCardNumber(), errors, TARGET_CARD_NUMBER, ERROR_CODE_TARGET_CARD_NUMBER);
        if (!creditCardTransactionDto.getSumOfMoney().toString().matches(moneyRegexp)) {
            errors.rejectValue(SUM_OF_MONEY, ERROR_CODE_SUM_OF_MONEY);
        }
    }

    private void validateCardNumber(String cardNumber, Errors errors, String field, String errorCode) {
        if(!cardNumber.matches(numberRegexp) || !bankDataGenerator.isCardNumberValid(cardNumber)) {
            errors.rejectValue(field, errorCode);
        }
    }

}

