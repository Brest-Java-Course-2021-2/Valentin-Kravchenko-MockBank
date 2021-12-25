package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.SumOfMoneyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the given sum of money
 *  is specified in the correct format.
 */
@Documented
@Constraint(validatedBy = {SumOfMoneyValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SumOfMoney {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
