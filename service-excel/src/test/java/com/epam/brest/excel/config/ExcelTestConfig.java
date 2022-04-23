package com.epam.brest.excel.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.epam.brest.excel",
                "com.epam.brest.faker"})
@EnableConfigurationProperties
public class ExcelTestConfig {
}
