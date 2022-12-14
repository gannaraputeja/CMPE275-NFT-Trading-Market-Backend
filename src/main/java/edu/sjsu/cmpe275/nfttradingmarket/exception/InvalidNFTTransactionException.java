package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class InvalidNFTTransactionException extends RuntimeException{
    public InvalidNFTTransactionException(String message) {
        super(message);
    }
}
