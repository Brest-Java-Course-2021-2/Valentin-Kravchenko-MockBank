package com.epam.brest.service.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

@TestConfiguration
public class ServiceRestTestConfig {

    private final WebApplicationContext webApplicationContext;

    public ServiceRestTestConfig(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @Bean
    public WebTestClient webTestClient() {
        return MockMvcWebTestClient.bindToApplicationContext(webApplicationContext).build();
    }

}
