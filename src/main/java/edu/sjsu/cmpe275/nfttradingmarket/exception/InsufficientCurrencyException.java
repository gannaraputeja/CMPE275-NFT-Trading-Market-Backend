package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class InsufficientCurrencyException extends RuntimeException{

    public InsufficientCurrencyException(String message) {
        super(message);
    }
}
