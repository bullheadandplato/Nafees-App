package com.bullhead.nafees.api.util.exception;

public class UnAuthorizedException extends Exception {
    public UnAuthorizedException() {
        super("user not authorized");
    }
}
