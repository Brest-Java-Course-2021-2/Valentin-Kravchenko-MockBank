package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.DifferentCardNumbersValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the target and source credit card numbers
 *  are different.
 */
@Documented
@Constraint(validatedBy = {DifferentCardNumbersValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentCardNumbers {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
