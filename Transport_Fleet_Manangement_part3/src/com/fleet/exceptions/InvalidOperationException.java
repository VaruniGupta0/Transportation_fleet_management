package com.fleet.exceptions;
// somethings that cannot happen
public class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super(message);
    }
}