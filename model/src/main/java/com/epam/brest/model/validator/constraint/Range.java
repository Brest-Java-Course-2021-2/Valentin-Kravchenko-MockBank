package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.RangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the start and end dates of the range are different and
 *  at least one of the dates is specified.
 */
@Documented
@Constraint(validatedBy = {RangeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Range{

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
