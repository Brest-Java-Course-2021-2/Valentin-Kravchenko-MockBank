package com.epam.brest.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class ServiceRestBasic {

    private final WebClient webClient;

    public ServiceRestBasic(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> T webClientGetBlock(String endpoint, ParameterizedTypeReference<T> elementTypeRef){
        return webClient.get()
                        .uri(endpoint)
                        .retrieve()
                        .bodyToMono(elementTypeRef)
                        .block();
    }

    public <T> T webClientPostBlock(String endpoint, Object data, ParameterizedTypeReference<T> elementTypeRef){
        return webClient.post()
                        .uri(endpoint)
                        .body(Mono.just(data), elementTypeRef)
                        .retrieve()
                        .bodyToMono(elementTypeRef)
                        .block();
    }

    public <T> T webClientPutBlock(String endpoint, Object data, ParameterizedTypeReference<T> elementTypeRef){
        return webClient.put()
                        .uri(endpoint)
                        .body(Mono.just(data), elementTypeRef)
                        .retrieve()
                        .bodyToMono(elementTypeRef)
                        .block();
    }

    public <T> T webClientDeleteBlock(String endpoint, ParameterizedTypeReference<T> elementTypeRef){
        return webClient.delete()
                        .uri(endpoint)
                        .retrieve()
                        .bodyToMono(elementTypeRef)
                        .block();
    }

}
