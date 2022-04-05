package com.epam.brest.restapp.exception;

import java.util.Map;

/**
 *  Response body containing the server error message or
 *  validation errors in the request body.
 */
public class ErrorResponse {

    private String errorMessage;

    private Map<String, String> validationErrors;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

}
