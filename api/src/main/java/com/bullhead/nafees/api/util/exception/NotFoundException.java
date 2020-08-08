package com.bullhead.nafees.api.util.exception;

public class NotFoundException extends Exception {
    public NotFoundException() {
        super("requested resource not found");
    }
}
