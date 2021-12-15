package com.epam.brest.service.api;

import com.epam.brest.model.dto.CreditCardTransactionDto;
import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.exception.CreditCardException;

import java.util.List;

public interface CreditCardService extends GenericService<CreditCard> {

    /**
     * Returns a credit card by its number.
     * @param number - credit card number
     * @return the credit card with the given number
     * @throws CreditCardException if none found
     */
    CreditCard getByNumber(String number) throws CreditCardException;

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
     * @throws CreditCardException if credit card has a positive balance
     */
    CreditCard delete(Integer id) throws CreditCardException;

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
     * @throws CreditCardException if source credit card doesn't contain enough money for the transfer
     */
    boolean transfer(CreditCardTransactionDto creditCardTransactionDto) throws CreditCardException;

    /**
     * Returns all the credit cards linked with a bank account by its id.
     * @param accountId - bank account id
     * @return list of credit cards
     */
    List<CreditCard> getAllByAccountId(Integer accountId);

}
