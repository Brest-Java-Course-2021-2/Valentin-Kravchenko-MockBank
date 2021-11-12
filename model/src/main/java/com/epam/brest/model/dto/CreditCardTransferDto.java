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

    public String getSourceCardNumber() {
        return sourceCardNumber;
    }

    public void setSourceCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
    }

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
