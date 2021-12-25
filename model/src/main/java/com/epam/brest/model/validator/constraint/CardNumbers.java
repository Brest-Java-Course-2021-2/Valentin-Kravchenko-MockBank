package com.epam.brest.model.validator.constraint;


import com.epam.brest.model.validator.CardNumbersValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the target and source credit card numbers
 *  are verified credit card number.
 */
@Documented
@Constraint(validatedBy = {CardNumbersValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CardNumbers {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
