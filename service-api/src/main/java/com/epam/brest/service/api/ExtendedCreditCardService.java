package com.epam.brest.service.api;

import com.epam.brest.model.entity.CreditCard;
import com.epam.brest.service.exception.ResourceNotFoundException;

public interface ExtendedCreditCardService extends CreditCardService {

    /**
     * Returns a credit card by its number.
     * @param number - credit card number
     * @return the credit card with the given number
     * @throws ResourceNotFoundException if the credit card is not found by its number
     */
    CreditCard getByNumber(String number) throws ResourceNotFoundException;

}
