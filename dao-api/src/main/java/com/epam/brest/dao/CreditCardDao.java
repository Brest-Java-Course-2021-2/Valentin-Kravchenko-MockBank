package com.epam.brest.dao;

import com.epam.brest.model.entity.CreditCard;

import java.util.List;

public interface CreditCardDao extends GenericDao<CreditCard> {

    /**
     * Return whether the credit card with specified number exists in db.
     * @param number - credit card number
     * @return true - if credit with specified number already exists, false - otherwise
     */
    boolean isCardNumberExists(String number);

    /**
     * Retrieves all credit cards linked with a bank account by its id.
     * @param accountId - bank account id
     * @return list of credit cards
     */
    List<CreditCard> getAllByAccountId(Integer accountId);

}
