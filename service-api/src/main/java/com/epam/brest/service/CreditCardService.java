package com.epam.brest.service;

import com.epam.brest.model.dto.CreditCardDepositDto;
import com.epam.brest.model.dto.CreditCardTransferDto;
import com.epam.brest.model.entity.CreditCard;

public interface CreditCardService {

    /**
     * Creates new credit card.
     * @param bankAccountId - id of the bank account to which the new credit card will be linked
     * @return the created credit card
     */
    CreditCard create(Integer bankAccountId);

    /**
     * Removes a given credit card.
     * @param creditCard - credit card being deleted
     * @return the number of rows affected
     */
    Integer delete(CreditCard creditCard);

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
