package com.epam.brest.dao.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.epam.brest.dao",
                "com.epam.brest.faker"})
public class DaoTestConfig {
}
