package com.epam.brest.model.validator;

import javax.validation.ConstraintValidatorContext;

public abstract class BasicValidator {

    public void buildConstraint(ConstraintValidatorContext context, String template, String property) {
        context.buildConstraintViolationWithTemplate(template).addPropertyNode(property).addConstraintViolation();
    }

}
