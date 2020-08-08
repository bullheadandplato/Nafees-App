package com.bullhead.nafees.api.util.exception;

public class NoInternetException extends Exception {
    public NoInternetException() {
        super("failed to connect to server. No internet");
    }
}
