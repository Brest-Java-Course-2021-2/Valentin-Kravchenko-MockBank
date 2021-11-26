package com.epam.brest.controller;

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

    @Value("${exception.error}")
    private String exceptionError;

    @Value("${handler.found.exception.error}")
    private String handlerError;

    @Value("${property.access.error}")
    private String propertyError;

    @ExceptionHandler({IllegalArgumentException.class})
    public String handleError(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR, ex.getMessage());
        return ex.getMessage().contains("account") ? REDIRECT_ACCOUNTS : REDIRECT_CARDS;
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public String handleError(HttpServletRequest request, Model model) {
        model.addAttribute(ERROR, String.format(handlerError, request.getRequestURI()));
        return ERROR;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public String handleError(HttpServletRequest request, MethodArgumentTypeMismatchException ex, Model model) {
        model.addAttribute(ERROR, String.format(propertyError, request.getRequestURI(), ex.getName(), ex.getValue()));
        return ERROR;
    }

    @ExceptionHandler({Exception.class})
    public String handleError(HttpServletRequest request, Exception ex, Model model) {
        model.addAttribute(ERROR, String.format(exceptionError, request.getRequestURI(), ex.getMessage()));;
        return ERROR;
    }

}
