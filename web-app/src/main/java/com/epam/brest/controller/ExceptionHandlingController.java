package com.epam.brest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.epam.brest.constant.ControllerConstant.*;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlingController.class);

    @Value("${exception.error}")
    private String exceptionError;

    @Value("${handler.found.exception.error}")
    private String handlerError;

    @Value("${property.access.error}")
    private String propertyError;

    @ExceptionHandler({IllegalArgumentException.class})
    public String handleError(Exception ex, RedirectAttributes redirectAttributes) {
        LOGGER.error("handleError(IllegalArgumentException, message={})", ex.getMessage());
        redirectAttributes.addFlashAttribute(ERROR, ex.getMessage());
        return ex.getMessage().contains("account") ? REDIRECT_ACCOUNTS : REDIRECT_CARDS;
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public String handleError(HttpServletRequest request, Model model) {
        String error = String.format(handlerError, request.getRequestURI());
        LOGGER.error("handleError(NoHandlerFoundException, message={})", error);
        model.addAttribute(ERROR, error);
        return ERROR;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public String handleError(HttpServletRequest request, MethodArgumentTypeMismatchException ex, Model model) {
        String error = String.format(propertyError, request.getRequestURI(), ex.getName(), ex.getValue());
        LOGGER.error("handleError(MethodArgumentTypeMismatchException, message={})", error);
        model.addAttribute(ERROR, error);
        return ERROR;
    }

    @ExceptionHandler({Exception.class})
    public String handleError(HttpServletRequest request, Exception ex, Model model) {
        String error = String.format(exceptionError, request.getRequestURI(), ex.getMessage());
        LOGGER.error("handleError(Exception, message={})", error);
        model.addAttribute(ERROR, error);
        return ERROR;
    }

}
