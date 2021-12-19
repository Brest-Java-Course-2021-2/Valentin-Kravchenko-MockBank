package com.epam.brest.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class ServiceRestBasic {

    public static final String SLASH = "/";

    private final WebClient webClient;

    public ServiceRestBasic(WebClient webClient) {
        this.webClient = webClient;
    }

    public WebClient.ResponseSpec getRetrieve(String endpoint){
        return webClient.get()
                        .uri(endpoint)
                        .retrieve();
    }

    public <T> T getBlock(String endpoint, ParameterizedTypeReference<T> parameterizedTypeRef){
        return getRetrieve(endpoint).bodyToMono(parameterizedTypeRef).block();
    }

    public WebClient.ResponseSpec postRetrieve(String endpoint, Object data){
        return webClient.post()
                        .uri(endpoint)
                        .body(Mono.just(data), data.getClass())
                        .retrieve();
    }

    public <T> T postBlock(String endpoint, Object data, ParameterizedTypeReference<T> parameterizedTypeRef){
        return postRetrieve(endpoint, data).bodyToMono(parameterizedTypeRef).block();
    }

    public WebClient.ResponseSpec putRetrieve(String endpoint, Object data){
        return webClient.put()
                        .uri(endpoint)
                        .body(Mono.just(data), data.getClass())
                        .retrieve();
    }

    public  WebClient.ResponseSpec deleteRetrieve(String endpoint){
        return webClient.delete()
                        .uri(endpoint)
                        .retrieve();
    }

}
