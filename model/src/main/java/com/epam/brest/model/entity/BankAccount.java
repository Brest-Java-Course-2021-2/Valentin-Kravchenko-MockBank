package com.epam.brest.model.entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 *  Bank account model
 */
public class BankAccount {
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

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          BankAccount that = (BankAccount) o;
          return Objects.equals(accountId, that.accountId) &&
                 Objects.equals(accountNumber, that.accountNumber) &&
                 Objects.equals(customer, that.customer) &&
                 Objects.equals(registrationDate, that.registrationDate);
     }

     @Override
     public int hashCode() {
          return Objects.hash(accountId, accountNumber, customer, registrationDate);
     }

}
