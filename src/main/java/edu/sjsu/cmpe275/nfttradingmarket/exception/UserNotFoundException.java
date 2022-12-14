package edu.sjsu.cmpe275.nfttradingmarket.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
