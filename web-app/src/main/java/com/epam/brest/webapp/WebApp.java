package com.epam.brest.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.epam.brest"})
public class WebApp  {

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }

}
