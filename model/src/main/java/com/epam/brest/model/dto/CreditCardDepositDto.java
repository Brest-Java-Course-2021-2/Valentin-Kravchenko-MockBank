package com.epam.brest.model.dto;

import java.math.BigDecimal;

/**
 *  Credit card data transfer object for depositing money.
 */
public class CreditCardDepositDto {

    /**
     *  Credit card number for depositing money.
     */
    private String cardNumber;

    /**
     *  Deposit sum of money.
     */
    private BigDecimal sumOfMoney;

}
