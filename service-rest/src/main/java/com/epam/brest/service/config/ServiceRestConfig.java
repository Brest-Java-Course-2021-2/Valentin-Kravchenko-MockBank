package com.epam.brest.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource({"classpath:service-rest.properties"})
public class ServiceRestConfig {

    @Bean
    public WebClient webClient(@Value("${base.url}") String baseUrl) {
        return WebClient.builder()
                        .baseUrl(baseUrl)
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build();
    }

}
