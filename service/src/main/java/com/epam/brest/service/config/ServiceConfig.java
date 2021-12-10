package com.epam.brest.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:service.properties"})
public class ServiceConfig {
}
