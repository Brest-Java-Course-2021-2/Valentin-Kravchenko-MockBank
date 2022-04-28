package com.epam.brest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 *  Credit card data model.
 */
@Entity
@Schema(description = "Credit card data model")
public class CreditCard extends BasicEntity {

    /**
     *  Credit card number.
     */
    @Schema(example = "4000003394112581", description = "Credit card number")
    @Column(length = 16, unique = true, nullable = false)
    private String number;

    /**
     *  Credit card expiration date.
     */
    @Schema(example = "2023-07-31", description = "Credit card expiration date")
    @Column(nullable = false)
    private LocalDate expirationDate;

    /**
     *  Credit card balance.
     */
    @Column(precision = 8, scale = 2)
    private BigDecimal balance;

    /**
     *  Bank account ID linked with the credit card.
     */
    @Schema(example = "1", description = "Bank account ID linked with the credit card")
    @Column(nullable = false)
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
        if (!(o instanceof CreditCard)) return false;
        if (!super.equals(o)) return false;
        CreditCard that = (CreditCard) o;
        return Objects.equals(number, that.number) &&
               Objects.equals(expirationDate, that.expirationDate) &&
               Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, expirationDate, accountId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+ "{" +
               "id='" + getId() + '\'' +
               ", number='" + number + '\'' +
               ", expirationDate=" + expirationDate +
               ", balance=" + balance +
               ", accountId=" + accountId +
               '}';
    }

}
