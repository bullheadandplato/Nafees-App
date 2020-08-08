package com.bullhead.nafees.android.ui.video;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bullhead.nafees.android.base.BaseRecyclerAdapter;
import com.bullhead.nafees.api.domain.Video;

import java.util.List;

public class VideoAdapter extends BaseRecyclerAdapter<Video, VideoViewHolder> {

    VideoAdapter(@NonNull List<Video> items) {
        super(items);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VideoViewHolder.create(parent);
    }
}
