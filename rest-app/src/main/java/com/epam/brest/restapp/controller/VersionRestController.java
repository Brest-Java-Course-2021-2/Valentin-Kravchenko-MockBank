package com.epam.brest.restapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Version API")
@RestController
@RequestMapping("api/version")
public class VersionRestController {

    @GetMapping
    public String version(@Value("${controller.version}") String version) {
        return version;
    }

}
