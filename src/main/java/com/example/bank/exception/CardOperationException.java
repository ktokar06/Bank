package com.example.bank.exception;

public class CardOperationException extends RuntimeException {
    public CardOperationException(String message) {
        super(message);
    }
}