package com.epam.brest.service;

import com.epam.brest.model.dto.CreditCardDto;

import java.util.List;

public interface CreditCardDtoService {

    /**
     * Retrieves all the credit cards, each with the linked bank account number.
     * @return list of credit cards
     */
    List<CreditCardDto> getAllWithAccountNumber();

}
