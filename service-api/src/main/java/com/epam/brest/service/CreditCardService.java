package com.epam.brest.service;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;

import java.util.List;

public interface CreditCardService {

    /**
     * Return a credit card by its id.
     * @param id - credit id
     * @return the credit card with the given id
     * @throws IllegalArgumentException if none found
     */
    CreditCard getById(Integer id);

    /**
     * Creates new credit card.
     * @param accountId - id of the bank account to which the new credit card will be linked
     * @return the created credit card
     */
    CreditCard create(Integer accountId);

    /**
     * Removes a given credit card.
     * @param creditCard - credit card being deleted
     * @return the number of rows affected
     * @throws IllegalArgumentException if credit card has a positive balance
     */
    Integer delete(CreditCard creditCard);

    /**
     * Deposits money to the credit card.
     * @param creditCardTransactionDto - CreditCardTransactionDto instance
     * @return true - if the transaction was successful, false - otherwise
     */
    boolean deposit(CreditCardTransactionDto creditCardTransactionDto);

    /**
     * Transfers money between credit cards.
     * @param creditCardTransactionDto - CreditCardTransactionDto instance
     * @return true - if the transaction was successful, false - otherwise
     * @throws IllegalArgumentException if source credit card doesn't contain enough money for the transfer
     */
    boolean transfer(CreditCardTransactionDto creditCardTransactionDto);

    /**
     * Return all credit cards linked with a bank account by its id.
     * @param accountId - bank account id
     * @return list of credit cards
     */
    List<CreditCard> getAllByAccountId(Integer accountId);

}
