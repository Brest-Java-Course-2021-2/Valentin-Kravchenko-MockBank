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

import javax.servlet.http.HttpServletResponse;

import static com.epam.brest.constant.ControllerConstant.*;
import static javax.servlet.http.HttpServletResponse.*;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlingController.class);

    @Value("${handler.found.exception.error}")
    private String handlerError;

    @Value("${property.access.error}")
    private String propertyError;

    @ExceptionHandler({IllegalArgumentException.class})
    public String handleError(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        LOGGER.warn("handleError(IllegalArgumentException.class, message={})", e.getMessage());
        redirectAttributes.addFlashAttribute(ERROR, e.getMessage());
        return e.getMessage().contains("account") ? REDIRECT_ACCOUNTS : REDIRECT_CARDS;
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public String handleError(HttpServletResponse httpServletResponse, NoHandlerFoundException e, Model model) {
        LOGGER.error("handleError(NoHandlerFoundException.class)", e);
        httpServletResponse.setStatus(SC_NOT_FOUND);
        model.addAttribute(ERROR, handlerError);
        return ERROR;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public String handleError(HttpServletResponse httpServletResponse, MethodArgumentTypeMismatchException e, Model model) {
        LOGGER.error("handleError(MethodArgumentTypeMismatchException.class)", e);
        httpServletResponse.setStatus(SC_BAD_REQUEST);
        String error = String.format(propertyError, e.getName(), e.getValue());
        model.addAttribute(ERROR, error);
        return ERROR;
    }

    @ExceptionHandler({Exception.class})
    public String handleError(HttpServletResponse httpServletResponse, Exception e, Model model) {
        LOGGER.error("handleError(Exception.class)", e);
        httpServletResponse.setStatus(SC_INTERNAL_SERVER_ERROR);
        model.addAttribute(ERROR, e.getMessage());
        return ERROR;
    }

}
