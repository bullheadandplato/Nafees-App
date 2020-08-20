package com.bullhead.nafees.android.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class NotificationSubscribeManager {
    private static final String TAG = NotificationSubscribeManager.class.getSimpleName();

    private static final String TOPIC = "com.bullhead.nafees.new_video";
    private final FirebaseMessaging messaging;

    @Inject
    NotificationSubscribeManager(@NonNull NotificationPrefs notificationPrefs) {
        this.messaging = FirebaseMessaging.getInstance();
        if (notificationPrefs.areNotificationsOn()) {
            subscribe();
        }
    }

    public void subscribe() {
        messaging.subscribeToTopic(TOPIC)
                .addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        Log.e(TAG, "subscribe: Failed to subscribe " +
                                "to notifications :( because " +
                                task.getException().getLocalizedMessage());
                    } else {
                        Log.i(TAG, "subscribe: Successfully subscribed to notifications.");
                    }
                });
    }

    public void unsubscribe() {
        messaging.unsubscribeFromTopic(TOPIC)
                .addOnCompleteListener(task -> {
                    if (task.getException() != null) {
                        Log.e(TAG, "unsubscribe: Failed to unsubscribe " +
                                "from notifications :( because " +
                                task.getException().getLocalizedMessage());
                    } else {
                        Log.i(TAG, "unsubscribe: Successfully unsubscribed from notifications.");
                    }
                });
    }
}
