package com.epam.brest.service.impl;

import com.epam.brest.model.CreditCardTransactionDto;
import com.epam.brest.model.CreditCard;
import com.epam.brest.service.ServiceRestBasic;
import com.epam.brest.service.api.CreditCardService;
import com.epam.brest.service.exception.CreditCardException;
import com.epam.brest.service.exception.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class CreditCardServiceRest extends ServiceRestBasic implements CreditCardService {

    private static final Logger LOGGER = LogManager.getLogger(CreditCardServiceRest.class);

    public static final String DEPOSIT_ENDPOINT = "/deposit";
    public static final String TRANSFER_ENDPOINT = "/transfer";

    private final String endpoint;
    private final ParameterizedTypeReference<CreditCard> parameterizedTypeRef;

    public CreditCardServiceRest(WebClient webClient, String endpoint) {
        super(webClient);
        this.endpoint = endpoint;
        parameterizedTypeRef = new ParameterizedTypeReference<>() {};
    }

    @Override
    public CreditCard create(Integer accountId) {
        LOGGER.debug("create(endpoint={})", endpoint);
        return postBlock(endpoint, accountId, parameterizedTypeRef);
    }

    @Override
    public CreditCard delete(Integer id) {
        LOGGER.debug("delete(endpoint={})", endpoint + SLASH + id);
        return deleteRetrieve(endpoint + SLASH + id)
                .onStatus(HttpStatus::is4xxClientError, this::getMonoException)
                .bodyToMono(parameterizedTypeRef)
                .block();
    }

    @Override
    public CreditCard deposit(CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("deposit(endpoint={}, creditCardTransactionDto={})", endpoint, creditCardTransactionDto);
        return postRetrieve(endpoint + DEPOSIT_ENDPOINT, creditCardTransactionDto)
                .onStatus(HttpStatus::is4xxClientError, this::getMonoException)
                .bodyToMono(parameterizedTypeRef)
                .block();
    }

    @Override
    public CreditCard transfer(CreditCardTransactionDto creditCardTransactionDto) {
        LOGGER.debug("transfer(endpoint={}, creditCardTransactionDto={})", endpoint, creditCardTransactionDto);
        return postRetrieve(endpoint + TRANSFER_ENDPOINT, creditCardTransactionDto)
                .onStatus(HttpStatus::is4xxClientError, this::getMonoException)
                .bodyToMono(parameterizedTypeRef)
                .block();
    }

    @Override
    public CreditCard getById(Integer id) {
        LOGGER.debug("getById(endpoint={})", endpoint + SLASH + id);
        return getRetrieve(endpoint + SLASH + id)
                .onStatus(HttpStatus::is4xxClientError, this::getMonoException)
                .bodyToMono(parameterizedTypeRef)
                .block();
    }

    private Mono<Throwable> getMonoException(ClientResponse response) {
        return response.bodyToMono(ErrorResponse.class).flatMap(errorResponse -> Mono.error(new CreditCardException(errorResponse.getMessage())));
    }

}
