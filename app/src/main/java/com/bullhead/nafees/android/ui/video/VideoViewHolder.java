package com.bullhead.nafees.android.ui.video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.base.BaseViewHolder;
import com.bullhead.nafees.android.databinding.ItemVideoBinding;
import com.bullhead.nafees.api.domain.Video;
import com.bumptech.glide.Glide;

public class VideoViewHolder extends BaseViewHolder<Video> {
    private ItemVideoBinding binding;

    private VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemVideoBinding.bind(itemView);
    }

    static VideoViewHolder create(@NonNull ViewGroup parent) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false));
    }

    @Override
    public <X extends Video> void bind(@NonNull X t) {
        Glide.with(binding.imageView)
                .load(t.getThumbnail())
                .placeholder(R.drawable.ic_twotone_eco_24)
                .centerCrop()
                .into(binding.imageView);
        binding.titleTextView.setText(t.getTitle());
    }
}
