package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class InsufficientCurrencyException extends RuntimeException{
    private final String message;


    public InsufficientCurrencyException(String message) {
        this.message = message;
    }
}
