package com.revature.banking.exceptions;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException() {
        super("Not enough balance");
    }
}
