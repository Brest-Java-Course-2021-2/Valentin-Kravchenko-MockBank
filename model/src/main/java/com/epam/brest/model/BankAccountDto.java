package com.epam.brest.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 *  Bank account data transfer object.
 */
@Schema(description = "Bank account data transfer object")
public class BankAccountDto extends BasicEntity {

    /**
     *  International bank account number.
     */
    @Schema(example = "BY80F29S8416E1PXLF9VHCGM99T6", description = "International bank account number")
    private String number;

    /**
     *  Full name of the bank customer holding the bank account.
     */
    @Schema(example = "Ivan Ivanov", description = "Full name of the bank customer holding the bank account")
    private String customer;

    /**
     *  Date of the customer registration in the bank system.
     */
    @Schema(example = "2020-06-28", description = "Date of the customer registration in the bank syste")
    private LocalDate registrationDate;

    /**
     *  Total cards linked to the bank account.
     */
    @Schema(example = "2", description = "Total cards linked to the bank account")
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "number='" + number + '\'' +
               ", customer='" + customer + '\'' +
               ", registrationDate=" + registrationDate +
               ", totalCards=" + totalCards +
               '}';
    }

}
