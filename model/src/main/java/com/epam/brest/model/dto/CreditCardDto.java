package com.epam.brest.model.dto;

import com.epam.brest.model.BasicEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *  Credit card data transfer object.
 */
public class CreditCardDto extends BasicEntity {

    /**
     *  Credit card number.
     */
    private String number;

    /**
     *  Credit card expiration date.
     */
    private LocalDate expirationDate;

    /**
     *  Credit card balance.
     */
    private BigDecimal balance;

    /**
     *  ID of the Bank account linked with the credit card.
     */
    private Integer accountId;

    /**
     *   Number of the bank account linked with the credit card.
     */
    private String accountNumber;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

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
}
