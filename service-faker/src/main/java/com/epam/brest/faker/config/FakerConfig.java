package com.epam.brest.faker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:faker.properties"})
public class FakerConfig {

    @Bean
    @ConfigurationProperties(prefix = "faker")
    public FakerSettings fakerSettings() {
        return new FakerSettings();
    }

}
