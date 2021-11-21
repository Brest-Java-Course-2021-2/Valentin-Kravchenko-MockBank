package com.epam.brest.model.dto;

import java.math.BigDecimal;

/**
 *  Credit card data transfer object for depositing money.
 */
public class CreditCardDepositDto {

    /**
     *  Number of the target credit card.
     */
    private String targetCardNumber;

    /**
     *  Deposit sum of money.
     */
    private BigDecimal sumOfMoney;

    public String getTargetCardNumber() {
        return targetCardNumber;
    }

    public void setTargetCardNumber(String targetCardNumber) {
        this.targetCardNumber = targetCardNumber;
    }

    public BigDecimal getSumOfMoney() {
        return sumOfMoney;
    }

    public void setSumOfMoney(BigDecimal sumOfMoney) {
        this.sumOfMoney = sumOfMoney;
    }

}
