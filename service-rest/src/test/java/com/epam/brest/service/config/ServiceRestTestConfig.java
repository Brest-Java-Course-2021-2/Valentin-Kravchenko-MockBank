package com.epam.brest.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@Configuration
public class ServiceRestTestConfig {

    @Bean
    public WebTestClient webClient() {
        return WebTestClient.bindToServer()
                            .baseUrl("http://localhost:8090")
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();
    }

}
