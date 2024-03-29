package com.epam.brest.restapp.exception;

import com.epam.brest.service.exception.BankAccountException;
import com.epam.brest.service.exception.CreditCardException;
import com.epam.brest.service.exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlingRestController {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlingRestController.class);

    @Value("${handler.found.exception.error}")
    private String noHandlerFoundErrorMessage;

    @Value("${property.access.error}")
    private String argumentTypeErrorTemplate;

    @Value("${http.message.readable.error}")
    private String httpMessageNotReadableErrorMessage;

    @ExceptionHandler({BankAccountException.class, CreditCardException.class, ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleError(RuntimeException e) {
        LOGGER.warn("handleError[BankAccountException, or CreditCardException, or ResourceNotFoundException], message={}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(e.getMessage());
        if (e instanceof ResourceNotFoundException) {
            return new ResponseEntity<>(errorResponse, NOT_FOUND);
        }
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponse> handleError(NoHandlerFoundException e) {
        LOGGER.warn("handleError[NoHandlerFoundException], message={}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(noHandlerFoundErrorMessage);
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleError(MethodArgumentTypeMismatchException e) {
        LOGGER.warn("handleError[MethodArgumentTypeMismatchException], message={}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        String message = String.format(argumentTypeErrorTemplate, e.getName(), e.getValue());
        errorResponse.setErrorMessage(message);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleError(HttpMessageNotReadableException e) {
        LOGGER.warn("handleError[HttpMessageNotReadableException], message={}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(httpMessageNotReadableErrorMessage);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleError(MethodArgumentNotValidException e) {
        LOGGER.warn("handleError[MethodArgumentNotValidException], message={}", e.getMessage());
        Map<String, String> validationErrors = e.getBindingResult()
                                                .getFieldErrors()
                                                .stream()
                                                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setValidationErrors(validationErrors);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleError(Exception e) {
        LOGGER.error("handleError[Exception]", e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(INTERNAL_SERVER_ERROR.getReasonPhrase());
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

}
