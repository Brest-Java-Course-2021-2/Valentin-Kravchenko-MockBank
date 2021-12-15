package com.epam.brest.dao.api;

import com.epam.brest.model.entity.CreditCard;

import java.util.List;

public interface CreditCardDao extends GenericDao<CreditCard> {

    /**
     * Returns whether the credit card with a given number exists in db.
     * @param number - credit card number
     * @return true - if credit with specified number already exists, false - otherwise
     */
    boolean isCardNumberExists(String number);

    /**
     * Retrieves all the credit cards linked with a bank account by its id.
     * @param accountId - bank account id
     * @return the list of credit cards
     */
    List<CreditCard> getAllByAccountId(Integer accountId);

}
