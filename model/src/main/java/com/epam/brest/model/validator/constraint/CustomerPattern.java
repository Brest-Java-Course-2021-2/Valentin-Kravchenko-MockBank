package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.CustomerSearchPattenValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates the search pattern for the customer.
 */
@Documented
@Constraint(validatedBy = {CustomerSearchPattenValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomerPattern {

    String message() default "{CustomerPattern.account.dto.customerPattern}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
