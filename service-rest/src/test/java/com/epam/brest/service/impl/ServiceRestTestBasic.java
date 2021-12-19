package com.epam.brest.service.impl;

import com.epam.brest.service.config.ServiceRestTestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

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

    <T> WebTestClient.ResponseSpec postAndExchange(String endpoint, T element){
        return webTestClient.post()
                            .uri(endpoint)
                            .body(Mono.just(element), element.getClass())
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json");
    }

    <T> WebTestClient.ResponseSpec postAndExpectStatusOk(String endpoint, T element){
        return postAndExchange(endpoint, element).expectStatus().isOk();
    }

    <T> WebTestClient.ResponseSpec putAndExchange(String endpoint, T element) {
        return webTestClient.put()
                            .uri(endpoint)
                            .body(Mono.just(element), element.getClass())
                            .exchange()
                            .expectHeader().valueEquals("Content-Type", "application/json");
    }

    <T> WebTestClient.ResponseSpec putAndExpectStatusOk(String endpoint, T element) {
        return putAndExchange(endpoint, element).expectStatus().isOk();
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
