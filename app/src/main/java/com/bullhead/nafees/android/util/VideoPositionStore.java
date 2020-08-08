package com.bullhead.nafees.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.bullhead.nafees.android.BuildConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class VideoPositionStore {
    public static final long              NO_POSITION = -1;
    private final       SharedPreferences prefs;

    @Inject
    VideoPositionStore(@NonNull Context context) {
        prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID + ".video_positions", Context.MODE_PRIVATE);
    }

    public float get(@NonNull String videoId) {
        return prefs.getFloat(videoId, -1);
    }

    public void set(@NonNull String videoId, float pos) {
        prefs.edit()
                .putFloat(videoId, pos)
                .apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    public void remove(long id) {
        prefs.edit().remove(String.valueOf(id)).apply();
    }
}
