package com.epam.brest.dao;

import com.epam.brest.model.entity.CreditCard;

public interface CreditCardDao extends GenericDao<CreditCard> {
    /**
     * Return whether the credit card with specified number exists in db
     * @param number - credit card number
     * @return true - if credit with specified number already exists, false - otherwise
     */
    boolean isCardNumberExists(String number);

}
