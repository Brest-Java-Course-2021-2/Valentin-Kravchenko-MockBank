package com.epam.brest.model.dto;

import java.time.LocalDate;

public class BankAccountDto {
    /**
     *  Bank account ID
     */
    private Integer accountId;

    /**
     *  International bank account number
     */
    private String accountNumber;

    /**
     *  Full name of the bank customer holding the bank account
     */
    private String customer;

    /**
     *  Date of the customer registration in the bank system
     */
    private LocalDate registrationDate;

    /**
     *  Total cards linked to the bank account
     */
    private Integer totalCards;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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
