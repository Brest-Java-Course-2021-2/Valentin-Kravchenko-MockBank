package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.AnyOfPatternsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that any of the patterns are specified.
 */
@Documented
@Constraint(validatedBy = {AnyOfPatternsValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyOfPatterns {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
