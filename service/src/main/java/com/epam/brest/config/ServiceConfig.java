package com.epam.brest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.epam.brest")
@PropertySource({"classpath:service.properties"})
public class ServiceConfig {
}
