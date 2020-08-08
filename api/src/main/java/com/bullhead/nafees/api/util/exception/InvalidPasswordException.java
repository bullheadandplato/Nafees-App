package com.bullhead.nafees.api.util.exception;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException() {
        super("wrong password");
    }
}
