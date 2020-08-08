package com.bullhead.nafees.api;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Api {
    private final AppService appService;

    @Inject
    public Api(@NonNull AppService appService) {
        this.appService = appService;
    }


}
