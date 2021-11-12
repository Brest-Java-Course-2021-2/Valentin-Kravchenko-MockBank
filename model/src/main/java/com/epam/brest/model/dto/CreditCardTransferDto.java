package com.epam.brest.model.dto;

import java.math.BigDecimal;

/**
 *  Credit card data transfer object for transferring money.
 */
public class CreditCardTransferDto {

    /**
     *  Number of the source credit card.
     */
    private String sourceCardNumber;

    /**
     *  Number of the target credit card.
     */
    private String targetCardNumber;

    /**
     *  Transfer sum of money.
     */
    private BigDecimal sumOfMoney;

}
