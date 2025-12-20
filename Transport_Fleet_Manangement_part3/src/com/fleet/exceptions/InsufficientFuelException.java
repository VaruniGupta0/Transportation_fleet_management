package com.fleet.exceptions;
//when not enough fuel to run the vehicle

public class InsufficientFuelException extends Exception{
    public InsufficientFuelException(String message) {
        super(message);
    }
}

