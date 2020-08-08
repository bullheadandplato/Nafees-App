package com.bullhead.nafees.api.util.exception;

public class SettingNotFoundException extends Exception {
    public SettingNotFoundException() {
        super("Provider settings with SettingStore before doing anything.");
    }
}
