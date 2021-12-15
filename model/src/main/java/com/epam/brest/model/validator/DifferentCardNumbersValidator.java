package com.epam.brest.model.validator;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.validator.constraint.DifferentCardNumbers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.epam.brest.model.constant.ModelConstant.DIFFERENT_CARD_NUMBERS_TEMPLATE;
import static com.epam.brest.model.constant.ModelConstant.TARGET_CARD_NUMBER;

public class DifferentCardNumbersValidator extends BasicValidator implements ConstraintValidator<DifferentCardNumbers, CreditCardTransactionDto> {

    private static final Logger LOGGER = LogManager.getLogger(DifferentCardNumbersValidator.class);

    @Override
    public boolean isValid(CreditCardTransactionDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (Objects.nonNull(value.getSourceCardNumber())) {
            if (Objects.equals(value.getSourceCardNumber(), value.getTargetCardNumber())) {
                HibernateConstraintValidatorContext validatorContext = context.unwrap(HibernateConstraintValidatorContext.class);
                buildConstraint(validatorContext, DIFFERENT_CARD_NUMBERS_TEMPLATE, TARGET_CARD_NUMBER);
                return false;
            }
        }
        return true;
    }

}
