package com.epam.brest.webapp.controller;

import com.epam.brest.service.exception.BankAccountException;
import com.epam.brest.service.exception.CreditCardException;
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

import static com.epam.brest.webapp.constant.ControllerConstant.*;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlingController.class);

    @Value("${handler.found.exception.error}")
    private String noHandlerFoundError;

    @Value("${property.access.error}")
    private String argumentTypeErrorTemplate;

    @ExceptionHandler({BankAccountException.class})
    public String handleError(BankAccountException e, RedirectAttributes redirectAttributes) {
        LOGGER.warn("handleBankAccountException, message={}", e.getMessage());
        redirectAttributes.addFlashAttribute(ERROR, e.getMessage());
        return REDIRECT_ACCOUNTS;
    }

    @ExceptionHandler({CreditCardException.class})
    public String handleError(CreditCardException e, RedirectAttributes redirectAttributes) {
        LOGGER.warn("handleCreditCardException, message={}", e.getMessage());
        redirectAttributes.addFlashAttribute(ERROR, e.getMessage());
        return REDIRECT_CARDS;
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public String handleError(HttpServletResponse httpServletResponse,
                              NoHandlerFoundException e,
                              Model model) {
        LOGGER.error("handleNoHandlerFoundException", e);
        httpServletResponse.setStatus(NOT_FOUND.value());
        model.addAttribute(ERROR, noHandlerFoundError);
        return ERROR;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public String handleError(HttpServletResponse httpServletResponse,
                              MethodArgumentTypeMismatchException e,
                              Model model) {
        LOGGER.error("handleMethodArgumentTypeMismatchException", e);
        httpServletResponse.setStatus(BAD_REQUEST.value());
        String error = String.format(argumentTypeErrorTemplate, e.getName(), e.getValue());
        model.addAttribute(ERROR, error);
        return ERROR;
    }

    @ExceptionHandler({Exception.class})
    public String handleError(HttpServletResponse httpServletResponse, Exception e, Model model) {
        LOGGER.error("handleException", e);
        httpServletResponse.setStatus(INTERNAL_SERVER_ERROR.value());
        model.addAttribute(ERROR, INTERNAL_SERVER_ERROR.getReasonPhrase());
        return ERROR;
    }

}
