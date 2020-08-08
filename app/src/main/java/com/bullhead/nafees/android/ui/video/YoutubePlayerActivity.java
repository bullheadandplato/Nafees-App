package com.bullhead.nafees.android.ui.video;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.bullhead.androidyoutubeplayer.core.player.YouTubePlayer;
import com.bullhead.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.bullhead.nafees.android.base.BaseActivity;
import com.bullhead.nafees.android.databinding.ActivityYoutubePlayerBinding;
import com.bullhead.nafees.android.util.SimpleYoutubePlayerListener;
import com.bullhead.nafees.android.util.VideoPositionStore;
import com.bullhead.nafees.api.domain.Video;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class YoutubePlayerActivity extends BaseActivity {
    @Inject
    VideoPositionStore positionStore;
    private ActivityYoutubePlayerBinding binding;
    private Video                        video;
    private YouTubePlayer                player;
    private SimpleYoutubePlayerListener  playerListener;

    public static void show(@NonNull Context context,
                            @NonNull Video video) {
        context.startActivity(new Intent(context, YoutubePlayerActivity.class)
                .putExtra("video", video));
    }

    @Override
    protected void onUserLeaveHint() {
        goPip();
    }

    private void goPip() {
        if (shouldEnterPip()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                enterPictureInPictureMode();
            }
        } else {
            super.onUserLeaveHint();
        }
    }

    @Override
    protected void onPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!isInPictureInPictureMode()) {
                binding.youtubePlayerView.onStop();
            }
        }
        super.onPause();
    }

    private boolean shouldEnterPip() {
        return playerListener != null && playerListener.shouldEnterPip();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYoutubePlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        goFullscreen();
        onNewIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        video = (Video) getIntent().getSerializableExtra("video");
        if (video == null) {
            finish();
            return;
        }
        setTitle(video.getTitle());
        float savedPosition = positionStore.get(video.getId());
        binding.youtubePlayerView.getPlayerUiController().showYouTubeButton(false);
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
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

    private void goFullscreen() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }
}
