package com.epam.brest.model.validator.constraint;


import com.epam.brest.model.validator.CreditCardValidator;
import com.epam.brest.model.validator.constant.CreditCardTransactionType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the target and source credit card numbers
 *  are verified credit card number.
 */
@Documented
@Constraint(validatedBy = {CreditCardValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreditCard {

    CreditCardTransactionType value();
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
