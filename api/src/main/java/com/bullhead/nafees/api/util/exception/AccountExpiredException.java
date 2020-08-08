package com.bullhead.nafees.api.util.exception;

public class AccountExpiredException extends Exception {
    public AccountExpiredException() {
        super("Account expired");
    }
}
