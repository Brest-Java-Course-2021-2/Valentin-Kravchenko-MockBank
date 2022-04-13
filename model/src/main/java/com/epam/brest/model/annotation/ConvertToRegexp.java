package com.epam.brest.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Converts a field expression to a regexp using the specified pattern.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertToRegexp {

    /**
     *  Regexp pattern.
     */
    String pattern() default "(?=.*%s)";
    
}
