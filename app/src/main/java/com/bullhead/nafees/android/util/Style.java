package com.bullhead.nafees.android.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bullhead.nafees.android.R;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class Style {
    private int secondaryColor;
    private int primaryColor;
    private int primaryDarkColor;

    @Inject
    public Style(@NonNull Context context) {
        init(context);
    }

    public void init(@NonNull Context context) {
        primaryColor     = ContextCompat.getColor(context, R.color.primaryColor);
        secondaryColor   = ContextCompat.getColor(context, R.color.secondaryColor);
        primaryDarkColor = ContextCompat.getColor(context, R.color.primaryDarkColor);
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }
}
