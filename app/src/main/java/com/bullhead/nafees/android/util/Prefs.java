package com.bullhead.nafees.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.bullhead.nafees.android.BuildConfig;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Prefs {
    private final SharedPreferences sharedPreferences;
    private final Gson              transformer;

    @Inject
    Prefs(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + ".prefs", Context.MODE_PRIVATE);
        transformer       = new Gson();
    }


    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
