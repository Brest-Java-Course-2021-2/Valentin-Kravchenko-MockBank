package com.epam.brest.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.epam.brest.webapp.constant.ControllerConstant.REDIRECT_ACCOUNTS;

@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping()
    public String redirect() {
        return REDIRECT_ACCOUNTS;
    }

}
