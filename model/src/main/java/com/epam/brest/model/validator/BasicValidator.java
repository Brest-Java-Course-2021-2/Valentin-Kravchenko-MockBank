package com.epam.brest.model.validator;

import javax.validation.ConstraintValidatorContext;

public abstract class BasicValidator {

    public void buildConstraintViolation(ConstraintValidatorContext context, String template, String property) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(template).addPropertyNode(property).addConstraintViolation();
    }

    public void buildConstraintViolation(ConstraintValidatorContext context, String template) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(template).addConstraintViolation();
    }

}
