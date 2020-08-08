package com.bullhead.nafees.android.util;

import com.bullhead.androidyoutubeplayer.core.player.PlayerConstants;
import com.bullhead.androidyoutubeplayer.core.player.YouTubePlayer;
import com.bullhead.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;

import org.jetbrains.annotations.NotNull;

public class SimpleYoutubePlayerListener implements YouTubePlayerListener {
    private PlayerConstants.PlayerState state;

    @Override
    public void onReady(@NotNull YouTubePlayer youTubePlayer) {

    }

    public boolean shouldEnterPip() {
        return state != null && state == PlayerConstants.PlayerState.PLAYING ||
                state == PlayerConstants.PlayerState.BUFFERING ||
                state == PlayerConstants.PlayerState.PAUSED ||
                state == PlayerConstants.PlayerState.VIDEO_CUED;
    }

    @Override
    public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState state) {
        this.state = state;
    }

    @Override
    public void onPlaybackQualityChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackQuality playbackQuality) {

    }

    @Override
    public void onPlaybackRateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlaybackRate playbackRate) {

    }

    @Override
    public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError error) {

    }

    @Override
    public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float second) {

    }

    @Override
    public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float duration) {

    }

    @Override
    public void onVideoLoadedFraction(@NotNull YouTubePlayer youTubePlayer, float loadedFraction) {

    }

    @Override
    public void onVideoId(@NotNull YouTubePlayer youTubePlayer, @NotNull String videoId) {

    }

    @Override
    public void onApiChange(@NotNull YouTubePlayer youTubePlayer) {

    }
}
