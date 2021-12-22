package com.epam.brest.service.impl;

import com.epam.brest.restapp.RestApp;
import com.epam.brest.service.config.ServiceRestTestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = {RestApp.class, ServiceRestTestConfig.class})
public class ServiceRestTestBasic {

    public static final String API_ENDPOINT = "/api";

    private final WebTestClient webTestClient;

    public ServiceRestTestBasic(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    WebTestClient.ResponseSpec getAndExpectStatusOk(String endpoint){
        return webTestClient.get()
                            .uri(getUri(endpoint))
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json")
                            .expectStatus().isOk();
    }

    <T> WebTestClient.ResponseSpec postAndExchange(String endpoint, T element){
        return webTestClient.post()
                            .uri(getUri(endpoint))
                            .body(Mono.just(element), element.getClass())
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json");
    }

    <T> WebTestClient.ResponseSpec postAndExpectStatusOk(String endpoint, T element){
        return postAndExchange(endpoint, element).expectStatus().isOk();
    }

    <T> WebTestClient.ResponseSpec putAndExchange(String endpoint, T element) {
        return webTestClient.put()
                            .uri(getUri(endpoint))
                            .body(Mono.just(element), element.getClass())
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json");
    }

    <T> WebTestClient.ResponseSpec putAndExpectStatusOk(String endpoint, T element) {
        return putAndExchange(endpoint, element).expectStatus().isOk();
    }

    WebTestClient.ResponseSpec deleteAndExchange(String endpoint) {
        return webTestClient.delete()
                            .uri(getUri(endpoint))
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json");
    }

    WebTestClient.ResponseSpec deleteAndExpectStatusOk(String endpoint) {
        return deleteAndExchange(endpoint).expectStatus().isOk();
    }

    private String getUri(String endpoint) {
        return API_ENDPOINT + endpoint;
    }

}
