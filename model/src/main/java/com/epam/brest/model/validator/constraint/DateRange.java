package com.epam.brest.model.validator.constraint;

import com.epam.brest.model.validator.DateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  Validates that the start and end dates of the range are verified date.
 */
@Documented
@Constraint(validatedBy = {DateRangeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRange {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
