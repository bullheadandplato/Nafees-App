package com.bullhead.nafees.android.ui.video;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bullhead.nafees.android.base.BaseRecyclerAdapter;
import com.bullhead.nafees.api.domain.Video;

import java.util.List;

public class VideoAdapter extends BaseRecyclerAdapter<Video, VideoViewHolder> {
    private final VideoViewHolder.FavoriteClickListener favoriteClickListener;
    private final VideoViewHolder.FavoriteCheckProvider favoriteCheckProvider;

    VideoAdapter(@NonNull List<Video> items,
                 @NonNull VideoViewHolder.FavoriteClickListener favoriteClickListener,
                 @NonNull VideoViewHolder.FavoriteCheckProvider favoriteCheckProvider) {
        super(items);
        this.favoriteClickListener = favoriteClickListener;
        this.favoriteCheckProvider = favoriteCheckProvider;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VideoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setFavoriteClickListener(favoriteClickListener);
        holder.setFavoriteCheckProvider(favoriteCheckProvider);
        super.onBindViewHolder(holder, position);
    }
}
