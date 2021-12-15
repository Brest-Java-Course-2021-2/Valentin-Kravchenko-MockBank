package com.epam.brest.model.validator;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.validator.constraint.CardNumbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.epam.brest.model.constant.ModelConstant.*;

public class CardNumbersValidator extends BasicValidator implements ConstraintValidator<CardNumbers, CreditCardTransactionDto> {

    private static final Logger LOGGER = LogManager.getLogger(DifferentCardNumbersValidator.class);

    private final BankDataGenerator bankDataGenerator;

    public CardNumbersValidator(BankDataGenerator bankDataGenerator) {
        this.bankDataGenerator = bankDataGenerator;
    }

    @Value("${card.number.regexp}")
    private String cardNumberRegexp;

    @Override
    public boolean isValid(CreditCardTransactionDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        String sourceCardNumber = value.getSourceCardNumber();
        String targetCardNumber = value.getTargetCardNumber();
        boolean isSourceCardNumberValid = true;
        boolean isTargetCardNumberValid = true;
        if (Objects.nonNull(sourceCardNumber)) {
            if (isCardNumberInvalid(sourceCardNumber)) {
                buildConstraint(context, SOURCE_CARD_NUMBER_TEMPLATE, SOURCE_CARD_NUMBER);
                isSourceCardNumberValid = false;
            }
        }
        if(isCardNumberInvalid(targetCardNumber)) {
            buildConstraint(context, TARGET_CARD_NUMBER_TEMPLATE, TARGET_CARD_NUMBER);
            isTargetCardNumberValid = false;
        }
        return isSourceCardNumberValid && isTargetCardNumberValid;
    }

    private boolean isCardNumberInvalid(String cardNumber) {
        LOGGER.info("isCardNumberInvalid(cardNumber={})", cardNumber);
        return !cardNumber.matches(cardNumberRegexp) || !bankDataGenerator.isCardNumberValid(cardNumber);
    }

}
