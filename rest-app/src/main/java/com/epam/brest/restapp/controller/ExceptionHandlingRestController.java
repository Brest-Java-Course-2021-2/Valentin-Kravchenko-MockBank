package com.epam.brest.restapp.controller;

import com.epam.brest.restapp.exception.ErrorResponse;
import com.epam.brest.service.exception.BankAccountException;
import com.epam.brest.service.exception.CreditCardException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionHandlingRestController {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlingRestController.class);

    @Value("${handler.found.exception.error}")
    private String noHandlerFoundErrorMessage;

    @Value("${property.access.error}")
    private String argumentTypeErrorTemplate;

    @ExceptionHandler({BankAccountException.class, CreditCardException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleError(RuntimeException e) {
        LOGGER.warn("handleServiceException, message={}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleError(NoHandlerFoundException e) {
        LOGGER.error("handleNoHandlerFoundException", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(noHandlerFoundErrorMessage);
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleError(MethodArgumentTypeMismatchException e) {
        LOGGER.error("handleMethodArgumentTypeMismatchException", e);
        ErrorResponse errorResponse = new ErrorResponse();
        String message = String.format(argumentTypeErrorTemplate, e.getName(), e.getValue());
        errorResponse.setMessage(message);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleError(MethodArgumentNotValidException e) {
        LOGGER.warn("handleMethodArgumentNotValidException", e);
        Map<String, String> validationErrors = e.getBindingResult()
                                                .getFieldErrors()
                                                .stream()
                                                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setValidationErrors(validationErrors);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<Void> handleError(Exception e) {
        LOGGER.error("handleException", e);
        return ResponseEntity.internalServerError().build();
    }

}
