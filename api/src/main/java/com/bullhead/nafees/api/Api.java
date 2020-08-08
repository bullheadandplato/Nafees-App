package com.bullhead.nafees.api;

import androidx.annotation.NonNull;

import com.bullhead.nafees.api.domain.Video;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class Api {
    private final AppService appService;

    @Inject
    public Api(@NonNull AppService appService) {
        this.appService = appService;
    }

    public Single<List<Video>> videos() {
        return appService.videos();
    }


}
