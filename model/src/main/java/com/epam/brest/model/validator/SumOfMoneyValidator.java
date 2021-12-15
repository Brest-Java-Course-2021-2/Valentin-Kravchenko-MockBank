package com.epam.brest.model.validator;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.validator.constraint.SumOfMoney;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.number.NumberStyleFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.text.ParseException;

import static com.epam.brest.model.constant.ModelConstant.SUM_OF_MONEY_TEMPLATE;
import static com.epam.brest.model.constant.ModelConstant.VALUE_SUM_OF_MONEY;

public class SumOfMoneyValidator extends BasicValidator implements ConstraintValidator<SumOfMoney, CreditCardTransactionDto> {

    private static final Logger LOGGER = LogManager.getLogger(SumOfMoneyValidator.class);

    private final NumberStyleFormatter numberStyleFormatter;

    @Value("${card.sum.money.regexp}")
    private String sumOfMoneyRegexp;

    public SumOfMoneyValidator(NumberStyleFormatter numberStyleFormatter) {
        this.numberStyleFormatter = numberStyleFormatter;
    }

    @Override
    public boolean isValid(CreditCardTransactionDto value, ConstraintValidatorContext context) {
        LOGGER.debug("isValid(value={})", value);
        HibernateConstraintValidatorContext validatorContext = context.unwrap(HibernateConstraintValidatorContext.class);
        if(!value.getValueSumOfMoney().matches(sumOfMoneyRegexp)) {
            buildConstraint(validatorContext, SUM_OF_MONEY_TEMPLATE, VALUE_SUM_OF_MONEY);
            return false;
        }
        try {
            BigDecimal sumOfMoney = (BigDecimal) numberStyleFormatter.parse(value.getValueSumOfMoney(), value.getLocale());
            value.setSumOfMoney(sumOfMoney);
        } catch (ParseException e) {
            buildConstraint(validatorContext, SUM_OF_MONEY_TEMPLATE, VALUE_SUM_OF_MONEY);
            return false;
        }
        return true;
    }

}
