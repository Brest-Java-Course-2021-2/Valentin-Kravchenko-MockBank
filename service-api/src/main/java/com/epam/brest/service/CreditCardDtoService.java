package com.epam.brest.service;

import com.epam.brest.model.dto.CreditCardDepositDto;
import com.epam.brest.model.dto.CreditCardDto;
import com.epam.brest.model.dto.CreditCardTransferDto;

import java.util.List;

public interface CreditCardDtoService {

    /**
     * Retrieves all the credit cards, each with the linked bank account number.
     * @return list of credit cards
     */
    List<CreditCardDto> getAllWithAccountNumber();

    /**
     * Deposits money to the credit card.
     * @param creditCardDepositDto - CreditCardDepositDto instance
     * @return true - if the transaction was successful, false - otherwise
     */
    boolean deposit(CreditCardDepositDto creditCardDepositDto);

    /**
     * Transfers money between credit cards.
     * @param creditCardTransferDto - CreditCardTransferDto instance
     * @return true - if the transaction was successful, false - otherwise
     */
    boolean transfer(CreditCardTransferDto creditCardTransferDto);

}
