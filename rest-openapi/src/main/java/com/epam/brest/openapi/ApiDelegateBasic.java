package com.epam.brest.openapi;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public abstract class ApiDelegateBasic {

    private final Validator validator;

    protected ApiDelegateBasic(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
