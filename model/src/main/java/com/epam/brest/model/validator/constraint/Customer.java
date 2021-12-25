package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.CustomerValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the customer has a well-formed full name.
 */
@Documented
@Constraint(validatedBy = {CustomerValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Customer {

    String message() default "{Customer.account.customer}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}