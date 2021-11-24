package com.epam.brest.controller;

import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.epam.brest.constant.ControllerConstant.*;

@ControllerAdvice
public class ExceptionHandlingController {
    
    @Value("${property.access.error}")
    private String propertyError;

    @ExceptionHandler({IllegalArgumentException.class})
    public String handleError(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR, ex.getMessage());
        return ex.getMessage().contains("account") ? REDIRECT_ACCOUNTS : REDIRECT_CARDS;
    }

    @ExceptionHandler({PropertyAccessException.class})
    public String handleError(HttpServletRequest request, Model model, MethodArgumentTypeMismatchException ex) {
        Object uri = request.getRequestURI();
        model.addAttribute(ERROR,  String.format(propertyError, uri, ex.getName(), ex.getValue()));
        return ERROR;
    }

}
