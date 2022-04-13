package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.DifferentCardsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the target and source credit card numbers
 *  are different.
 */
@Documented
@Constraint(validatedBy = {DifferentCardsValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentCards {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
