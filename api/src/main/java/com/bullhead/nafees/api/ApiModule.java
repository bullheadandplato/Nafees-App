package com.bullhead.nafees.api;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Provides
    static AppService appService(@NonNull Retrofit retrofit) {
        return retrofit.create(AppService.class);
    }
}
