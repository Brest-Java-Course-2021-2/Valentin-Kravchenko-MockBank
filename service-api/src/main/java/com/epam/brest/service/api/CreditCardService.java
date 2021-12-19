package com.epam.brest.service.api;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.exception.CreditCardException;

public interface CreditCardService extends GenericService<CreditCard> {

    /**
     * Creates a new credit card.
     * @param accountId - id of the bank account to which the new credit card will be linked
     * @return the instance of the created credit card
     */
    CreditCard create(Integer accountId);

    /**
     * Removes a credit card by its id.
     * @param id - id of the credit card to delete
     * @return the instance of the deleted credit card
     * @throws CreditCardException if credit card has a positive balance or
     * if the credit card is not found by its id
     */
    CreditCard delete(Integer id) throws CreditCardException;

    /**
     * Deposits money to the credit card.
     * @param creditCardTransactionDto - CreditCardTransactionDto instance
     * @return the instance of the target credit card after a deposit transaction
     * @throws CreditCardException if the target credit card is not found
     * by its number
     */
    CreditCard deposit(CreditCardTransactionDto creditCardTransactionDto) throws CreditCardException;

    /**
     * Transfers money between credit cards.
     * @param creditCardTransactionDto - CreditCardTransactionDto instance
     * @return the instance of the source credit card after a transfer transaction
     * @throws CreditCardException if the source credit card doesn't contain enough money for the transfer
     * or credit cards were not found by their numbers
     */
    CreditCard transfer(CreditCardTransactionDto creditCardTransactionDto) throws CreditCardException;

}
