package com.bullhead.nafees.android.ui.video;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bullhead.nafees.android.base.DataFragment;
import com.bullhead.nafees.android.helper.Helper;
import com.bullhead.nafees.api.Api;
import com.bullhead.nafees.api.domain.Video;
import com.bullhead.nafees.api.util.exception.ApiExceptionUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class VideoFragment extends DataFragment<Video> {
    @Inject
    Api service;

    @NonNull
    public static VideoFragment newInstance() {
        Bundle        args     = new Bundle();
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public RecyclerView.Adapter<VideoViewHolder> getAdapter(@NonNull List<Video> data) {
        VideoAdapter adapter = new VideoAdapter(data);
        adapter.setListener((item, pos) -> {

        });
        return adapter;
    }

    @NonNull
    @Override
    public Observable<List<Video>> getCall() {
        return service.videos()
                .onErrorResumeNext(error -> Single.error(ApiExceptionUtil.generalException(error)))
                .toObservable();
    }

    @NonNull
    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(context, Helper.getSpanCountByItemWidth(250));
    }
}
