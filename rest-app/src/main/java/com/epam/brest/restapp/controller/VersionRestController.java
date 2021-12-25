package com.epam.brest.restapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/version")
public class VersionRestController {

    @GetMapping
    public String version(@Value("${controller.version}") String version) {
        return version;
    }

}