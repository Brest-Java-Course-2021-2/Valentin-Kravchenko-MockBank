package com.epam.brest.model;

import com.epam.brest.model.validator.constraint.Customer;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.Objects;

/**
 *  Bank account data model.
 */
@Entity
@Schema(description = "Bank account data model")
public class BankAccount extends BasicEntity {

     /**
      *  International bank account number.
      */
     @Schema(example = "BY80F29S8416E1PXLF9VHCGM99T6", description = "International bank account number")
     @Column(length = 32, unique = true, nullable = false)
     private String number;

     /**
      *  Full name of the bank customer holding the bank account.
      */
     @Customer
     @Schema(example = "Ivan Ivanov", description = "Full name of the bank customer holding the bank account")
     @Column(length = 128, nullable = false)
     private String customer;

     /**
      *  Date of the customer registration in the bank system.
      */
     @Schema(example = "2020-06-28", description = "Date of the customer registration in the bank system")
     @Column(nullable = false)
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

     @Override
     public String toString() {
          return getClass().getSimpleName() + "{" +
                 "id='" + getId() + '\'' +
                 ", number='" + number + '\'' +
                 ", customer='" + customer + '\'' +
                 ", registrationDate=" + registrationDate +
                 '}';
     }

}
