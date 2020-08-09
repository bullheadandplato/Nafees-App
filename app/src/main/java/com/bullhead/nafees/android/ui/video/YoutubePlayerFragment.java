package com.bullhead.nafees.android.ui.video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bullhead.androidyoutubeplayer.core.player.YouTubePlayer;
import com.bullhead.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.bullhead.nafees.android.base.BaseFragment;
import com.bullhead.nafees.android.databinding.ActivityYoutubePlayerBinding;
import com.bullhead.nafees.android.util.SimpleYoutubePlayerListener;
import com.bullhead.nafees.android.util.VideoPositionStore;
import com.bullhead.nafees.api.domain.Video;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class YoutubePlayerFragment extends BaseFragment {
    @Inject
    VideoPositionStore positionStore;
    private ActivityYoutubePlayerBinding binding;
    private Video                        video;
    private SimpleYoutubePlayerListener  playerListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityYoutubePlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void play(@NonNull Video video) {
        this.video = video;
        float savedPosition = positionStore.get(video.getId());
        binding.youtubePlayerView.getPlayerUiController().showYouTubeButton(false);
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(video.getId(), savedPosition);
                playerListener = new SimpleYoutubePlayerListener() {
                    @Override
                    public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float second) {
                        positionStore.set(video.getId(), second);
                    }
                };
                youTubePlayer.addListener(playerListener);
            }
        });
        getLifecycle().addObserver(binding.youtubePlayerView);
    }

    public boolean shouldEnterPip() {
        return playerListener != null && playerListener.shouldEnterPip();
    }
}
