package com.epam.brest.service.exception;

public class BankAccountException extends IllegalArgumentException {

    public BankAccountException(String message) {
        super(message);
    }

}
