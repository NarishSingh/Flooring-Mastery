package com.sg.flooringmastery.dao;

public class OrderPersistenceException extends Exception {

    public OrderPersistenceException(String message) {
        super(message);
    }

    public OrderPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
