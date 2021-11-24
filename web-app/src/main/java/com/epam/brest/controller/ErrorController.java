package com.epam.brest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static com.epam.brest.constant.ControllerConstant.ERROR;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @Value("${handler.exception.error}")
    private String handlerError;

    @GetMapping
    public String handle(HttpServletRequest request, Model model) {
        Object uri = request.getAttribute("javax.servlet.error.request_uri");
        Object code = request.getAttribute("javax.servlet.error.status_code");
        Object message = request.getAttribute("javax.servlet.error.message");
        model.addAttribute(ERROR, String.format(handlerError, code, uri, message));
        return ERROR;
    }

}
