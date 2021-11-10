package com.epam.brest.dao;

import com.epam.brest.model.dto.CreditCardDto;

import java.util.List;

public interface CreditCardDtoDao {
    /**
     * Retrieves all the credit cards, each with the linked bank account number
     *
     * @return list of credit cards
     */
    List<CreditCardDto> getAllWithAccountNumber();

}
