package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class NoOffersFoundForListingException extends RuntimeException{
    public NoOffersFoundForListingException(String message){
        super(message);
    }
}
