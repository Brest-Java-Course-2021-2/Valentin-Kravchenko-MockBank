package com.epam.brest.model.dto;

import java.math.BigDecimal;

/**
 *  Credit card data transfer object for performing transaction.
 */
public class CreditCardTransactionDto {

    /**
     *  Number of the target credit card.
     */
    private String targetCardNumber;

    /**
     *  Deposit sum of money.
     */
    private BigDecimal sumOfMoney;

    /**
     *  Number of the source credit card.
     */
    private String sourceCardNumber;

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

    public String getSourceCardNumber() {
        return sourceCardNumber;
    }

    public void setSourceCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
    }
}
