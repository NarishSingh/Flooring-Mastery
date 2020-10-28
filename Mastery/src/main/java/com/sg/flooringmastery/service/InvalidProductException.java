package com.sg.flooringmastery.service;

public class InvalidProductException extends Exception {

    public InvalidProductException(String message) {
        super(message);
    }

    public InvalidProductException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
