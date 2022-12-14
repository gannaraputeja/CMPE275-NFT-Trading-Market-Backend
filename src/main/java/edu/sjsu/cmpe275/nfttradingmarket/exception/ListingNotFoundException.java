package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class ListingNotFoundException extends RuntimeException{
    public ListingNotFoundException(String message){
        super(message);
    }
}
