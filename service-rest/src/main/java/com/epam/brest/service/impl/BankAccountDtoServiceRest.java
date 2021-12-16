package com.epam.brest.service.impl;

import com.epam.brest.model.dto.BankAccountDto;
import com.epam.brest.model.dto.BankAccountFilterDto;
import com.epam.brest.service.api.BankAccountDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class BankAccountDtoServiceRest implements BankAccountDtoService {

    private static final Logger LOGGER = LogManager.getLogger(BankAccountDtoServiceRest.class);

    private final WebClient webClient;
    private final String endpoint;
    private final ParameterizedTypeReference<List<BankAccountDto>> parameterizedTypeReference;

    public BankAccountDtoServiceRest(WebClient webClient, String endpoint) {
        this.webClient = webClient;
        this.endpoint = endpoint;
        parameterizedTypeReference = new ParameterizedTypeReference<>() {};
    }

    @Override
    public List<BankAccountDto> getAllWithTotalCards() {
        LOGGER.debug("getAllWithTotalCards()");
        return webClient.get()
                        .uri(endpoint)
                        .retrieve()
                        .bodyToMono(parameterizedTypeReference)
                        .block();
    }

    @Override
    public List<BankAccountDto> getAllWithTotalCards(BankAccountFilterDto bankAccountFilterDto) {
        LOGGER.debug("getAllWithTotalCards(bankAccountFilterDto={})", bankAccountFilterDto);
        return webClient.post()
                        .uri(endpoint)
                        .body(Mono.just(bankAccountFilterDto), BankAccountFilterDto.class)
                        .retrieve()
                        .bodyToMono(parameterizedTypeReference)
                        .block();
    }

}
