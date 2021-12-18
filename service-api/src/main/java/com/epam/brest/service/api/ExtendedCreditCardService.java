package com.epam.brest.service.api;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.exception.CreditCardException;

import java.util.List;

public interface ExtendedCreditCardService extends CreditCardService {

    /**
     * Returns all the credit cards linked with a bank account by its id.
     * @param accountId - bank account id
     * @return list of credit cards
     */
    List<CreditCard> getAllByAccountId(Integer accountId);

    /**
     * Returns a credit card by its number.
     * @param number - credit card number
     * @return the credit card with the given number
     * @throws CreditCardException if none found
     */
    CreditCard getByNumber(String number) throws CreditCardException;

}
