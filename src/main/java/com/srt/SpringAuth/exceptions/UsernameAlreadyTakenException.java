package com.srt.SpringAuth.exceptions;

public class UsernameAlreadyTakenException extends Exception {
    public UsernameAlreadyTakenException(String message) {
        super(message);
    } 
}
