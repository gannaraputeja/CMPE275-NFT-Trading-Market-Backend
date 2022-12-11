package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class NoOffersFoundForListingException extends RuntimeException{
    private final String message;
    public NoOffersFoundForListingException(String message){
        this.message = message;
    }
}
