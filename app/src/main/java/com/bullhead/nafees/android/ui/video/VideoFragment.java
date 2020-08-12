package com.bullhead.nafees.android.ui.video;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.base.DataFragment;
import com.bullhead.nafees.android.helper.Helper;
import com.bullhead.nafees.android.ui.home.HomeActivity;
import com.bullhead.nafees.android.util.FavoriteVideoManager;
import com.bullhead.nafees.api.Api;
import com.bullhead.nafees.api.domain.Video;
import com.bullhead.nafees.api.util.exception.ApiExceptionUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoFragment extends DataFragment<Video> {
    private static final String TAG = VideoFragment.class.getSimpleName();

    @Inject
    Api                  service;
    @Inject
    FavoriteVideoManager favoriteManager;

    private CompositeDisposable disposables;
    private VideoAdapter        videoAdapter;
    private boolean             favorite;

    @NonNull
    public static VideoFragment newInstance(boolean favorite) {
        Bundle args = new Bundle();
        args.putBoolean("favorite", favorite);
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            favorite = getArguments().getBoolean("favorite");
        }
    }

    @NonNull
    @Override
    public RecyclerView.Adapter<VideoViewHolder> getAdapter(@NonNull List<Video> data) {
        videoAdapter = new VideoAdapter(data, this::toggleFavorite, this::isFavorite);
        videoAdapter.setListener((item, pos) -> {
            activity().ifPresent(ac -> {
                if (ac instanceof HomeActivity) {
                    ((HomeActivity) ac).play(item);
                }
            });
        });
        return videoAdapter;
    }

    private void toggleFavorite(@NonNull Video video, int pos) {
        Disposable d = favoriteManager.isFavorite(video)
                .flatMapCompletable(favorite ->
                        favorite ? favoriteManager.delete(video) : favoriteManager.insert(video))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (videoAdapter != null) {
                        videoAdapter.notifyItemChanged(pos);
                    }
                }, error -> {
                    Log.e(TAG, "toggleFavorite: " +
                            "Unable to toggle favorite because " + error.getLocalizedMessage());
                });
        disposables.add(d);
    }

    @NonNull
    private Single<Boolean> isFavorite(@NonNull Video video) {
        return favoriteManager.isFavorite(video);
    }

    @NonNull
    @Override
    public Observable<List<Video>> getCall() {
        return (favorite ? favoriteManager.get() : service.videos())
                .onErrorResumeNext(error -> Single.error(ApiExceptionUtil.generalException(error)))
                .toObservable();
    }

    @NonNull
    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(context, Helper.getSpanCountByItemWidth(250));
    }

    @Override
    public int getErrorIcon() {
        return favorite ? R.drawable.ic_round_favorite_24 : R.drawable.ic_twotone_eco_24;
    }

    @Override
    public void onStart() {
        super.onStart();
        disposables = new CompositeDisposable();
    }

    @Override
    public void onStop() {
        disposables.dispose();
        super.onStop();
    }
}
