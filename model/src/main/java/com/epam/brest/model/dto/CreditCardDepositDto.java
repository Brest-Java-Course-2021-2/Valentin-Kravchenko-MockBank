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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getSumOfMoney() {
        return sumOfMoney;
    }

    public void setSumOfMoney(BigDecimal sumOfMoney) {
        this.sumOfMoney = sumOfMoney;
    }

}
