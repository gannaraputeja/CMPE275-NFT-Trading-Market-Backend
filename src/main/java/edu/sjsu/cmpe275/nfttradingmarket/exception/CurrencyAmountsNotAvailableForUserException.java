package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class CurrencyAmountsNotAvailableForUserException extends RuntimeException{
    private final String message;


    public CurrencyAmountsNotAvailableForUserException(String message) {
        this.message = message;
    }
}
