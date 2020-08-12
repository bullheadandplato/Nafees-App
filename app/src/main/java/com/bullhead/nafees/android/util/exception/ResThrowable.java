package com.bullhead.nafees.android.util.exception;

import androidx.annotation.StringRes;

import com.bullhead.nafees.android.App;

public class ResThrowable extends Exception {
    public ResThrowable(@StringRes int res, Object... args) {
        super(App.getInstance().getString(res, args));
    }
}
