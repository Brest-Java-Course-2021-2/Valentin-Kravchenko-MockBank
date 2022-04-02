package com.epam.brest.service.api;

import com.epam.brest.model.CreditCardFilterDto;
import com.epam.brest.model.CreditCardDto;

import java.util.List;

public interface CreditCardDtoService {

    /**
     * Returns all the credit cards, each with the linked bank account number.
     * @return the list of credit cards
     */
    List<CreditCardDto> getAllWithAccountNumber();

    /**
     * Returns all the credit cards, each with the linked bank account number.
     * Return is carried out according to a given range of credit card expiration dates.
     * @param creditCardFilterDto - credit card date range instance
     * @return the list of credit cards
     */
    List<CreditCardDto> getAllWithAccountNumber(CreditCardFilterDto creditCardFilterDto);

}
