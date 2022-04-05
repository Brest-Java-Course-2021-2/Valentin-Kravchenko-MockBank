package com.epam.brest.openapi.exception;

import com.epam.brest.model.ErrorMessage;
import com.epam.brest.model.ValidationErrorsMessage;
import com.epam.brest.service.exception.BankAccountException;
import com.epam.brest.service.exception.CreditCardException;
import com.epam.brest.service.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionHandlingOpenApiController {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlingOpenApiController.class);

    @ExceptionHandler({BankAccountException.class, CreditCardException.class, ResourceNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleError(RuntimeException e) {
        LOGGER.warn("handleServiceException, message={}", e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(e.getMessage());
        if (e instanceof ResourceNotFoundException) {
            return new ResponseEntity<>(errorMessage, NOT_FOUND);
        }
        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorsMessage handleError(ConstraintViolationException e) {
        Map<String, String> validationErrors = e.getConstraintViolations()
                                                .stream()
                                                .collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(),
                                                                          ConstraintViolation::getMessage));

        ValidationErrorsMessage validationErrorsMessage = new ValidationErrorsMessage();
        validationErrorsMessage.setValidationErrors(validationErrors);
        return validationErrorsMessage;
    }
    
}
