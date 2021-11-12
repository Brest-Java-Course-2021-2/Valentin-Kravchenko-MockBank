package com.epam.brest.model.dto;

import com.epam.brest.model.BaseEntity;

import java.time.LocalDate;

/**
 *  Bank account data transfer object.
 */
public class BankAccountDto extends BaseEntity {

    /**
     *  International bank account number.
     */
    private String number;

    /**
     *  Full name of the bank customer holding the bank account.
     */
    private String customer;

    /**
     *  Date of the customer registration in the bank system.
     */
    private LocalDate registrationDate;

    /**
     *  Total cards linked to the bank account.
     */
    private Integer totalCards;

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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Integer getTotalCards() {
        return totalCards;
    }

    public void setTotalCards(Integer totalCards) {
        this.totalCards = totalCards;
    }

}
