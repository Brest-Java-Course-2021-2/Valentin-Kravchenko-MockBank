package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.TransactionAmountValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the given transaction amount
 *  is specified in the required format.
 */
@Documented
@Constraint(validatedBy = {TransactionAmountValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionAmount {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
