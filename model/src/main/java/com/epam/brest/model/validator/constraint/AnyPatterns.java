package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.AnyPatternsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that any of the patterns is not empty.
 */
@Documented
@Constraint(validatedBy = {AnyPatternsValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyPatterns {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
