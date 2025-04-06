package com.example.bank.exception;

public class InvalidLimitException extends RuntimeException {
    public InvalidLimitException(String message) {
        super(message);
    }
}