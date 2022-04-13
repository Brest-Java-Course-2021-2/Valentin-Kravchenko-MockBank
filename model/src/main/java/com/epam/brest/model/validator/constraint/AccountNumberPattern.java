package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.AccountNumberPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the search pattern for the account number has a required format.
 */
@Documented
@Constraint(validatedBy = {AccountNumberPatternValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountNumberPattern {

    String message() default "{AccountNumberPattern.account.dto.numberPattern}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
