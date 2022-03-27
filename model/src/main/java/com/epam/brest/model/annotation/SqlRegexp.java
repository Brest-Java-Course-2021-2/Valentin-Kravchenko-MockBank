package com.epam.brest.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Converts all the expressions in the annotated field value
 *  to a regexp using a specified pattern.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlRegexp {

    /**
     *  Regexp pattern.
     */
    String pattern() default "(?=.*%s)";
    
}
