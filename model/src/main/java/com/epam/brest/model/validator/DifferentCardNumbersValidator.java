package com.epam.brest.model.validator;

import com.epam.brest.model.CreditCardTransactionDto;
import com.epam.brest.model.validator.constraint.DifferentCardNumbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.epam.brest.model.validator.constant.ValidatorConstant.DIFFERENT_CARD_NUMBERS_TEMPLATE;
import static com.epam.brest.model.validator.constant.ValidatorConstant.TARGET_CARD_NUMBER;

public class DifferentCardNumbersValidator extends BasicValidator implements ConstraintValidator<DifferentCardNumbers, CreditCardTransactionDto> {

    private static final Logger LOGGER = LogManager.getLogger(DifferentCardNumbersValidator.class);

    @Override
    public boolean isValid(CreditCardTransactionDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (Objects.nonNull(value.getSourceCardNumber())) {
            if (Objects.equals(value.getSourceCardNumber(), value.getTargetCardNumber())) {
                buildConstraint(context, DIFFERENT_CARD_NUMBERS_TEMPLATE, TARGET_CARD_NUMBER);
                return false;
            }
        }
        return true;
    }

}
