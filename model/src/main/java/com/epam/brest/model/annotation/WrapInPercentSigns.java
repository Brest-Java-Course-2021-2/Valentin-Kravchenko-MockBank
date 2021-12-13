package com.epam.brest.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Indicates that a value of the annotated field
 *  must be wrap in percent signs
 *  to execute SQL as a LIKE parameter.
 *  Only for String type.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrapInPercentSigns {

    /**
     *  Template for wrapping in percent signs.
     */
    String template() default "%%%s%%";

}
