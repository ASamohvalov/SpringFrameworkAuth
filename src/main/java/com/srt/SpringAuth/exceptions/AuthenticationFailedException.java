package com.srt.SpringAuth.exceptions;

public class AuthenticationFailedException extends Exception {
    public AuthenticationFailedException(String message) {
        super(message);
    }        
}
