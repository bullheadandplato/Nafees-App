package com.bullhead.nafees.android.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bullhead.android.favorite.FavoriteHandler;
import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.util.exception.ResThrowable;
import com.bullhead.nafees.api.domain.Video;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;

@Singleton
public final class FavoriteVideoManager {
    private final FavoriteHandler<Video> favoriteHandler;
    private       boolean                changed;

    @Inject
    FavoriteVideoManager(@NonNull Context context) {
        favoriteHandler = new FavoriteHandler<>(context, Video.class);
    }

    @NonNull
    public Completable insert(@NonNull Video video) {
        return Completable.create(emitter -> {
            favoriteHandler.insert(video);
            changed = true;
            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    @NonNull
    public Completable delete(@NonNull Video video) {
        return Completable.create(emitter -> {
            favoriteHandler.delete(video);
            changed = true;
            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    @NonNull
    public Single<List<Video>> get() {
        return Single.create(emitter -> {
            List<Video> videos = favoriteHandler.getAll(Video.class);
            if (videos.isEmpty()) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new ResThrowable(R.string.no_favorite_videos));
                }
            } else {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(videos);
                }
            }
        });
    }

    @NonNull
    public Single<Boolean> isFavorite(@NonNull Video video) {
        return Single.create(emitter -> {
            boolean isFavorite = favoriteHandler.isFavorite(video);
            if (!emitter.isDisposed()) {
                emitter.onSuccess(isFavorite);
            }
        });
    }

    public boolean getStateAndInvalidate() {
        boolean toReturn = changed;
        changed = false;
        return toReturn;
    }
}
