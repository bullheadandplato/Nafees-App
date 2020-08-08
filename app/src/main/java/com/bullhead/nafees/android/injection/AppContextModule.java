package com.bullhead.nafees.android.injection;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bullhead.nafees.android.App;

import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {
    private final App app;

    public AppContextModule(@NonNull App app) {
        this.app = app;
    }

    @Provides
    Context getContext() {
        return app;
    }

    @Provides
    App app() {
        return app;
    }
}
