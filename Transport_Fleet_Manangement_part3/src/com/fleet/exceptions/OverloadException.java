package com.fleet.exceptions;

public class OverloadException extends Exception{// when extra thing or passenger are added than the load that vehicle can bear
    public OverloadException(String message) {
        super(message);
    }
}
