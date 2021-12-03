package com.epam.brest.model.dto;

import com.epam.brest.model.annotation.WrapInPercents;

/**
 *  Bank account data transfer object for filtering.
 */
public class BankAccountFilterDto {

    /**
     *  Search pattern for an international bank account number.
     */
    @WrapInPercents
    private String number;

    /**
     *  Search pattern for a full name of the bank customer.
     */
    @WrapInPercents
    private String customer;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "number='" + number + '\'' +
               ", customer='" + customer + '\'' +
               '}';
    }

}
