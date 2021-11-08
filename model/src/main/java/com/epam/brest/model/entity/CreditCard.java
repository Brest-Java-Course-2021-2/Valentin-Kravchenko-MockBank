package com.epam.brest.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *  Credit card model
 */
public class CreditCard {

    /**
     *  Credit card ID
     */
    private Integer cardId;

    /**
     *  Credit card number
     */
    private String cardNumber;

    /**
     *  Credit card expiration date
     */
    private LocalDate expirationDate;

    /**
     *  Credit card balance
     */
    private BigDecimal balance;

    /**
     *  Bank account ID linked with the credit card
     */
    private Integer accountId;

}
