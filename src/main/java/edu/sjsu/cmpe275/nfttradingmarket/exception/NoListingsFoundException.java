package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class NoListingsFoundException extends RuntimeException{

    public NoListingsFoundException(String message) {
        super(message);
    }
}
