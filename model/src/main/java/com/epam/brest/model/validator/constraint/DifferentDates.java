package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.DifferentDatesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the start and end dates of the range are different.
 */
@Documented
@Constraint(validatedBy = {DifferentDatesValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentDates {

    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
