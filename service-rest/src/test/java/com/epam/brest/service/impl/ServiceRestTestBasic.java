package com.epam.brest.service.impl;

import com.epam.brest.service.config.ServiceRestTestConfig;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = {ServiceRestTestConfig.class})
public class ServiceRestTestBasic {

    private final WebTestClient webTestClient;

    public ServiceRestTestBasic(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    WebTestClient.ResponseSpec getAndExpectStatusOk(String endpoint){
        return webTestClient.get()
                            .uri(endpoint)
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json")
                            .expectStatus().isOk();
    }

    <T, S extends Publisher<T>> WebTestClient.ResponseSpec postAndExchange(String endpoint, S publisher, Class<T> elementClass){
        return webTestClient.post()
                            .uri(endpoint)
                            .body(publisher, elementClass)
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json");
    }

    <T, S extends Publisher<T>> WebTestClient.ResponseSpec postAndExpectStatusOk(String endpoint, S publisher, Class<T> elementClass){
        return postAndExchange(endpoint, publisher, elementClass).expectStatus().isOk();
    }

    <T, S extends Publisher<T>> WebTestClient.ResponseSpec putAndExchange(String endpoint, S publisher, Class<T> elementClass) {
        return webTestClient.put()
                            .uri(endpoint)
                            .body(publisher, elementClass)
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json");
    }

    <T, S extends Publisher<T>> WebTestClient.ResponseSpec putAndExpectStatusOk(String endpoint, S publisher, Class<T> elementClass) {
        return putAndExchange(endpoint, publisher, elementClass).expectStatus().isOk();
    }

    WebTestClient.ResponseSpec deleteAndExchange(String endpoint) {
        return webTestClient.delete()
                            .uri(endpoint)
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json");
    }

    WebTestClient.ResponseSpec deleteAndExpectStatusOk(String endpoint) {
        return deleteAndExchange(endpoint).expectStatus().isOk();
    }

}
