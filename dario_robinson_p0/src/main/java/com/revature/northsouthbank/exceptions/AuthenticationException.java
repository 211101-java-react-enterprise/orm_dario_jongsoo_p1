package com.revature.northsouthbank.exceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("You have provided incorrect credentials. Please try again.");
    }
}
