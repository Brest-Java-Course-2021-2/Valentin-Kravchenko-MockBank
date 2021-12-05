package com.epam.brest.controller;

import com.epam.brest.exception.BankAccountException;
import com.epam.brest.exception.CreditCardException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

import static com.epam.brest.constant.ControllerConstant.*;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlingController.class);

    @Value("${handler.found.exception.error}")
    private String handlerError;

    @Value("${property.access.error}")
    private String propertyError;

    @ExceptionHandler({BankAccountException.class})
    public String handleError(BankAccountException e, RedirectAttributes redirectAttributes) {
        LOGGER.warn("handleError(BankAccountException.class, message={})", e.getMessage());
        redirectAttributes.addFlashAttribute(ERROR, e.getMessage());
        return REDIRECT_ACCOUNTS;
    }

    @ExceptionHandler({CreditCardException.class})
    public String handleError(CreditCardException e, RedirectAttributes redirectAttributes) {
        LOGGER.warn("handleError(CreditCardException.class, message={})", e.getMessage());
        redirectAttributes.addFlashAttribute(ERROR, e.getMessage());
        return REDIRECT_CARDS;
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public String handleError(HttpServletResponse httpServletResponse, NoHandlerFoundException e, Model model) {
        LOGGER.error("handleError(NoHandlerFoundException.class)", e);
        httpServletResponse.setStatus(NOT_FOUND.value());
        model.addAttribute(ERROR, handlerError);
        return ERROR;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public String handleError(HttpServletResponse httpServletResponse, MethodArgumentTypeMismatchException e, Model model) {
        LOGGER.error("handleError(MethodArgumentTypeMismatchException.class)", e);
        httpServletResponse.setStatus(BAD_REQUEST.value());
        String error = String.format(propertyError, e.getName(), e.getValue());
        model.addAttribute(ERROR, error);
        return ERROR;
    }

    @ExceptionHandler({Throwable.class})
    public String handleError(HttpServletResponse httpServletResponse, Throwable e, Model model) {
        LOGGER.error("handleError(Exception.class)", e);
        httpServletResponse.setStatus(INTERNAL_SERVER_ERROR.value());
        model.addAttribute(ERROR, INTERNAL_SERVER_ERROR.getReasonPhrase());
        return ERROR;
    }

}
