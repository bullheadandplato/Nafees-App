package com.bullhead.nafees.android.notification;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class NotificationPrefs {
    private final SharedPreferences preferences;

    @Inject
    NotificationPrefs(@NonNull Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean areNotificationsOn() {
        return preferences.getBoolean("notifications", true);
    }
}
