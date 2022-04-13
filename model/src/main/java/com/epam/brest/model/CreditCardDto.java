package com.epam.brest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *  Credit card data transfer object.
 */
@Schema(description = "Credit card data transfer object")
public class CreditCardDto extends BasicEntity {

    /**
     *  Credit card number.
     */
    @Schema(example = "4000003394112581", description = "Credit card number")
    private String number;

    /**
     *  Credit card expiration date.
     */
    @Schema(example = "2023-07-31", description = "Credit card expiration date")
    private LocalDate expirationDate;

    /**
     *  Credit card balance.
     */
    private BigDecimal balance;

    /**
     *   Bank account ID linked with the credit card.
     */
    @Schema(example = "1")
    private Integer accountId;

    /**
     *   Bank account number linked with the credit card.
     */
    @Schema(example = "BY80F29S8416E1PXLF9VHCGM99T6", description = "Bank account number linked with the credit card")
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "number='" + number + '\'' +
               ", expirationDate=" + expirationDate +
               ", balance=" + balance +
               ", accountId=" + accountId +
               ", accountNumber='" + accountNumber + '\'' +
               '}';
    }

}
