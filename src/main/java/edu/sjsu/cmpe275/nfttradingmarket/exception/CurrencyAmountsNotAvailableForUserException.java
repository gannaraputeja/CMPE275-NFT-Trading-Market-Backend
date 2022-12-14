package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class CurrencyAmountsNotAvailableForUserException extends RuntimeException{

    public CurrencyAmountsNotAvailableForUserException(String message) {
        super(message);
    }
}
