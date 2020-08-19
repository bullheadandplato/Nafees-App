package com.bullhead.nafees.android.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = NotificationService.class.getSimpleName();

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i(TAG, "onNewToken: Firebase token refreshed but I do not care :D");
    }
}
