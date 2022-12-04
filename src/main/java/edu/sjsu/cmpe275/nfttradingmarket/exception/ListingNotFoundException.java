package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class ListingNotFoundException extends RuntimeException{
    private final String message;
    public ListingNotFoundException(String message){
        this.message = message;
    }
}
