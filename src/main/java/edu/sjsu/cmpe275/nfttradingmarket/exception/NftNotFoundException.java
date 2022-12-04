package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class NftNotFoundException extends RuntimeException{
    private final String message;

    public NftNotFoundException(String message)
    {
        this.message = message;
    }
}
