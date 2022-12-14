package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class NftNotFoundException extends RuntimeException{

    public NftNotFoundException(String message) {
        super(message);
    }
}
