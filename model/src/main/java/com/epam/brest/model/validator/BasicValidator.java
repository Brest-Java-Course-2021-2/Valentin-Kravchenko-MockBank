package com.epam.brest.model.validator;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

public abstract class BasicValidator {

    public void buildConstraint(HibernateConstraintValidatorContext validatorContext, String template, String property) {
        validatorContext.buildConstraintViolationWithTemplate(template)
                        .addPropertyNode(property)
                        .addConstraintViolation();

    }

}
