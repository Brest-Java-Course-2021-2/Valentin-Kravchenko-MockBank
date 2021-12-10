package com.epam.brest.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.epam.brest.dao", "com.epam.brest.service"})
public class ServiceConfig {
}
