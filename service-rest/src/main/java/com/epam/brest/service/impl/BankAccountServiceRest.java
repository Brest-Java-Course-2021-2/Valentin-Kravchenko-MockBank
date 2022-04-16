package com.epam.brest.service.impl;

import com.epam.brest.model.BankAccount;
import com.epam.brest.model.CreditCard;
import com.epam.brest.service.ServiceRestBasic;
import com.epam.brest.service.api.BankAccountService;
import com.epam.brest.service.exception.BankAccountException;
import com.epam.brest.service.exception.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.String.format;

public class BankAccountServiceRest extends ServiceRestBasic implements BankAccountService {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountServiceRest.class);

    public static final String CARDS_ENDPOINT = "/cards";

    private final String endpoint;
    private final ParameterizedTypeReference<BankAccount> parameterizedTypeRef;
    private final ParameterizedTypeReference<List<CreditCard>> parameterizedTypeRefCreditCard;

    public BankAccountServiceRest(WebClient webClient, String endpoint) {
        super(webClient);
        this.endpoint = endpoint;
        parameterizedTypeRef = new ParameterizedTypeReference<>() {};
        parameterizedTypeRefCreditCard = new ParameterizedTypeReference<>() {};
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        LOGGER.debug("create(endpoint={})", endpoint);
        return postBlock(endpoint, bankAccount, parameterizedTypeRef);
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        LOGGER.debug("update(endpoint={})", endpoint);
        return putRetrieve(endpoint, bankAccount)
                .onStatus(HttpStatus::is4xxClientError, this::getMonoException)
                .bodyToMono(parameterizedTypeRef)
                .block();
    }

    @Override
    public BankAccount delete(Integer id) {
        LOGGER.debug("getById(endpoint={})", format(PATH_ID_TEMPLATE, endpoint, id));
        return deleteRetrieve(format(PATH_ID_TEMPLATE, endpoint, id))
                .onStatus(HttpStatus::is4xxClientError, this::getMonoException)
                .bodyToMono(parameterizedTypeRef)
                .block();
    }

    @Override
    public BankAccount getById(Integer id) {
        LOGGER.debug("getById(endpoint={})", format(PATH_ID_TEMPLATE, endpoint, id));
        return getRetrieve(format(PATH_ID_TEMPLATE, endpoint, id))
                .onStatus(HttpStatus::is4xxClientError, this::getMonoException)
                .bodyToMono(parameterizedTypeRef)
                .block();
    }

    @Override
    public List<CreditCard> getAllCardsById(Integer id) {
        LOGGER.debug("getAllCardsById(endpoint={})", format(PATH_ID_TEMPLATE, endpoint, id) + CARDS_ENDPOINT);
        return getBlock(format(PATH_ID_TEMPLATE, endpoint, id) + CARDS_ENDPOINT, parameterizedTypeRefCreditCard);
    }

    private Mono<Throwable> getMonoException(ClientResponse response) {
        return response.bodyToMono(ErrorResponse.class).flatMap(errorResponse -> Mono.error(new BankAccountException(errorResponse.getMessage())));
    }

}
