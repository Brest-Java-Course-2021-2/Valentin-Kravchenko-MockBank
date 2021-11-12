package com.epam.brest.service;

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

}
