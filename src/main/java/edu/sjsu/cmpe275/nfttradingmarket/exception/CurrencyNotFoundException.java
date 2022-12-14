package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class CurrencyNotFoundException extends RuntimeException{
    private final String message;


    public CurrencyNotFoundException(String message) {
        this.message = message;
    }
}
