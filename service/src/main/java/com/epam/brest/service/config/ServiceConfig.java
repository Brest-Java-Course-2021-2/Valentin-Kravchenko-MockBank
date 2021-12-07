package com.epam.brest.service.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({"com.epam.brest.service"})
@PropertySource({"classpath:service.properties"})
public class ServiceConfig {
}
