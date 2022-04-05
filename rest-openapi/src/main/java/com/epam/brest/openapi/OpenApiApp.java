package com.epam.brest.openapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.epam.brest"})
public class OpenApiApp {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiApp.class, args);
    }

}
