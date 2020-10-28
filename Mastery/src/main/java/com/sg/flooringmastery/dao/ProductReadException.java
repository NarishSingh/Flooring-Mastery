package com.sg.flooringmastery.dao;

public class ProductReadException extends Exception {

    public ProductReadException(String message) {
        super(message);
    }

    public ProductReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
