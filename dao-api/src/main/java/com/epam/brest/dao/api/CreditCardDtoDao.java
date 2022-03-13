package com.epam.brest.dao.api;

import com.epam.brest.model.dto.CreditCardsFilterDto;
import com.epam.brest.model.dto.CreditCardDto;

import java.util.List;

public interface CreditCardDtoDao {

    /**
     * Retrieves all the credit cards, each with the linked bank account number.
     * @return the list of credit cards
     */
    List<CreditCardDto> getAllWithAccountNumber();

    /**
     * Retrieves all the credit cards, each with the linked bank account number.
     * Retrieving is carried out according to a given range of credit card expiration dates.
     * @param creditCardsFilterDto - credit card date range instance
     * @return the list of credit cards
     */
    List<CreditCardDto> getAllWithAccountNumber(CreditCardsFilterDto creditCardsFilterDto);

}
