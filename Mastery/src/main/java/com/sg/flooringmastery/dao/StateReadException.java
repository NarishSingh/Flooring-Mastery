package com.sg.flooringmastery.dao;

public class StateReadException extends Exception {

    public StateReadException(String message) {
        super(message);
    }

    public StateReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
