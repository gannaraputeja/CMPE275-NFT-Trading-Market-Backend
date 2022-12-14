package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class InvalidNFTTransactionException extends RuntimeException{
    private final String message;
    public InvalidNFTTransactionException(String message) {
        this.message = message;
    }
}
