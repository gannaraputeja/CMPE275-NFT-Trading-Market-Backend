package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class NoListingsFoundException extends RuntimeException{
    private final String message;

    public NoListingsFoundException(String message)
    {
        this.message = message;
    }
}
