package com.epam.brest.model.entity;

import com.epam.brest.model.BaseEntity;

import java.time.LocalDate;
import java.util.Objects;

/**
 *  Bank account model.
 */
public class BankAccount extends BaseEntity {

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

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (!(o instanceof BankAccount)) return false;
          if (!super.equals(o)) return false;
          BankAccount that = (BankAccount) o;
          return Objects.equals(number, that.number) &&
                 Objects.equals(customer, that.customer) &&
                 Objects.equals(registrationDate, that.registrationDate);
     }

     @Override
     public int hashCode() {
          return Objects.hash(super.hashCode(), number, customer, registrationDate);
     }
}
