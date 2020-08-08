package com.bullhead.nafees.api;

import com.bullhead.nafees.api.domain.Video;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

interface AppService {
    @GET("app/videos")
    Single<List<Video>> videos();
}
