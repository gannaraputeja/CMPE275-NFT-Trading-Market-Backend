package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class OfferNotAvailabeException extends RuntimeException{
    private final String message;
    public OfferNotAvailabeException(String message){
        this.message = message;
    }
}
