package com.epam.brest.restapp.exception;

import java.util.Map;

public class ErrorResponse {

    private String message;

    private Map<String, String> validationErrors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

}
