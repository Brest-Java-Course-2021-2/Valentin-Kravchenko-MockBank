package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.CustomerPattenValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the search pattern for the customer has a required format.
 */
@Documented
@Constraint(validatedBy = {CustomerPattenValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomerPattern {

    String message() default "{CustomerPattern.account.dto.customerPattern}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
