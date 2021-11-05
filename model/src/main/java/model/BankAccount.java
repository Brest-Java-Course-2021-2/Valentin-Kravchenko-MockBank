package model;

import java.time.LocalDate;

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

}
