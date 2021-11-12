package com.epam.brest.model.entity;

import com.epam.brest.model.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 *  Credit card model.
 */
public class CreditCard extends BaseEntity {

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
     *  ID of the bank account linked with the credit card.
     */
    private Integer accountId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCard that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(number, that.number) &&
               Objects.equals(expirationDate, that.expirationDate) &&
               Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, expirationDate, accountId);
    }
}
