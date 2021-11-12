package com.epam.brest.generator;

public interface BankDataGenerator {

    /**
     * Returns a new international bank account number.
     * @return international bank account number
     */
    String generateIban();

    /**
     * Returns a new credit card number.
     * @return credit card number
     */
    String generateCardNumber();

    /**
     * Validates a given credit card number.
     * @param cardNumber - credit card number
     * @return true - if the credit card number is valid, false - otherwise
     */
    boolean isCardNumberValid(String cardNumber);

}
