package com.epam.brest.model.dto;

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
    private String sumOfMoney;

    /**
     *  Number of the source credit card.
     */
    private String sourceCardNumber;

    /**
     *  Current locale.
     */
    private Locale locale;

    public String getTargetCardNumber() {
        return targetCardNumber;
    }

    public void setTargetCardNumber(String targetCardNumber) {
        this.targetCardNumber = targetCardNumber;
    }

    public String getSumOfMoney() {
        return sumOfMoney;
    }

    public void setSumOfMoney(String sumOfMoney) {
        this.sumOfMoney = sumOfMoney;
    }

    public String getSourceCardNumber() {
        return sourceCardNumber;
    }

    public void setSourceCardNumber(String sourceCardNumber) {
        this.sourceCardNumber = sourceCardNumber;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+ "{" +
               "targetCardNumber='" + targetCardNumber + '\'' +
               ", sumOfMoney='" + sumOfMoney + '\'' +
               ", sourceCardNumber='" + sourceCardNumber + '\'' +
               ", locale=" + locale +
               '}';
    }
}
