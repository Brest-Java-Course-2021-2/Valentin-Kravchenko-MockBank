package com.epam.brest.model.dto;

import com.epam.brest.model.annotation.SqlColumn;
import com.epam.brest.model.annotation.WrapInPercentSigns;
import com.epam.brest.model.validator.constraint.AccountNumberPattern;
import com.epam.brest.model.validator.constraint.AnyPatterns;
import com.epam.brest.model.validator.constraint.CustomerPattern;

/**
 *  Bank account data transfer object for filtering.
 */
@AnyPatterns
public class BankAccountFilterDto {

    /**
     *  Search pattern for an international bank account number.
     */
    @WrapInPercentSigns
    @AccountNumberPattern
    @SqlColumn("number")
    private String numberPattern;

    /**
     *  Search pattern for a full name of the bank customer.
     */
    @WrapInPercentSigns
    @CustomerPattern
    @SqlColumn("customer")
    private String customerPattern;

    public String getNumberPattern() {
        return numberPattern;
    }

    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
    }

    public String getCustomerPattern() {
        return customerPattern;
    }

    public void setCustomerPattern(String customerPattern) {
        this.customerPattern = customerPattern;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "number='" + numberPattern + '\'' +
               ", customer='" + customerPattern + '\'' +
               '}';
    }

}
