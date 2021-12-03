package com.epam.brest.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Indicates that a value of the annotated field is used in a sql query for the LIKE parameter.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrapInPercents {

    /**
     *  Template for wrapping in percents.
     */
    String template() default "%%%s%%";
}
