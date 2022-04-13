package com.epam.brest.model.validator;

import com.epam.brest.generator.BankDataGenerator;
import com.epam.brest.model.validator.constant.CreditCardTransactionType;
import com.epam.brest.model.validator.constraint.CreditCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.epam.brest.model.validator.constant.CreditCardTransactionType.SOURCE;
import static com.epam.brest.model.validator.constant.CreditCardTransactionType.TARGET;
import static com.epam.brest.model.validator.constant.ValidatorConstant.*;

public class CreditCardValidator extends BasicValidator implements ConstraintValidator<CreditCard, String> {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardValidator.class);

    private final BankDataGenerator bankDataGenerator;

    private CreditCardTransactionType creditCardTransactionType;

    @Value("${card.number.regexp}")
    private String cardNumberRegexp;

    public CreditCardValidator(BankDataGenerator bankDataGenerator) {
        this.bankDataGenerator = bankDataGenerator;
    }

    @Override
    public void initialize(CreditCard constraintAnnotation) {
        creditCardTransactionType = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (Objects.isNull(value)) {
            return true;
        }
        if(!isCardNumberValid(value)) {
            if (creditCardTransactionType.equals(SOURCE)) {
                buildConstraintViolation(context, SOURCE_CARD_NUMBER_TEMPLATE);
            } else if (creditCardTransactionType.equals(TARGET)) {
                buildConstraintViolation(context, TARGET_CARD_NUMBER_TEMPLATE);
            }
            return false;
        }
        return true;
    }

    private boolean isCardNumberValid(String cardNumber) {
        LOGGER.info("isCardNumberInvalid(cardNumber={})", cardNumber);
        return cardNumber.matches(cardNumberRegexp) && bankDataGenerator.isCardNumberValid(cardNumber);
    }

}
