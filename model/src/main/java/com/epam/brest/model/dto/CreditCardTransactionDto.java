package com.epam.brest.model.dto;

import java.math.BigDecimal;
import java.util.Locale;

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

    /**
     *  Deposit sum of money from http request.
     */
    private String valueSumOfMoney;

    /**
     *  Locale from http request.
     */
    private Locale locale;

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

    public String getValueSumOfMoney() {
        return valueSumOfMoney;
    }

    public void setValueSumOfMoney(String valueSumOfMoney) {
        this.valueSumOfMoney = valueSumOfMoney;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "targetCardNumber='" + targetCardNumber + '\'' +
               ", sumOfMoney=" + sumOfMoney +
               ", sourceCardNumber='" + sourceCardNumber + '\'' +
               ", valueSumOfMoney='" + valueSumOfMoney + '\'' +
               ", locale=" + locale +
               '}';
    }

}
