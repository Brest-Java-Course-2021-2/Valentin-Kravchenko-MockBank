package com.epam.brest.model.validator;

import com.epam.brest.model.dto.CreditCardsFilterDto;
import com.epam.brest.model.validator.constraint.DifferentDates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.epam.brest.model.constant.ModelConstant.*;

public class DifferentDatesValidator extends BasicValidator implements ConstraintValidator<DifferentDates, CreditCardsFilterDto> {

    private static final Logger LOGGER = LogManager.getLogger(DifferentDatesValidator.class);

    @Override
    public boolean isValid(CreditCardsFilterDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        if (Objects.nonNull(value.getFromDate()) && Objects.nonNull(value.getToDate()) && value.getFromDate().equals(value.getToDate())) {
            buildConstraint(context, DIFFERENT_DATES_VALUE_FROM_DATE_TEMPLATE, VALUE_FROM_DATE);
            buildConstraint(context, DIFFERENT_DATES_VALUE_TO_DATE_TEMPLATE, VALUE_TO_DATE);
            return false;
        }
        return true;
    }

}
