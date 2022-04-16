package com.epam.brest.faker.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@ComponentScan({"com.epam.brest.faker"})
@EnableConfigurationProperties
public class FakerTestConfig {
}
