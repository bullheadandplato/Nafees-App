package com.bullhead.nafees.android.ui.video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bullhead.androidyoutubeplayer.core.player.YouTubePlayer;
import com.bullhead.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.bullhead.nafees.android.R;
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
    private boolean                      started;
    private BackPressListener            backPressListener;
    private OnCloseListener              closeListener;
    private boolean                      collapsed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            video = (Video) getArguments().getSerializable("video");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ActivityYoutubePlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void setVideo(Video video) {
        this.video = video;
        Bundle args = new Bundle();
        args.putSerializable("video", video);
        setArguments(args);
        if (started) {
            play(video);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.backButton.setOnClickListener(v -> onBackClick());
        binding.youtubePlayerView.getPlayerUiController().showYouTubeButton(false);
        getLifecycle().addObserver(binding.youtubePlayerView);
        if (video != null) {
            play(video);
        }
    }

    private void onBackClick() {
        if (collapsed) {
            if (closeListener != null) {
                closeListener.closePlayer();
            }
        }
        if (this.backPressListener != null) {
            backPressListener.onBack();
            collapse();
        }
    }

    @Override
    public void onDestroyView() {
        getLifecycle().removeObserver(binding.youtubePlayerView);
        binding = null;
        super.onDestroyView();
    }

    public void play(@NonNull Video video) {
        float savedPosition = positionStore.get(video.getId());
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
    }

    public boolean shouldEnterPip() {
        return playerListener != null && playerListener.shouldEnterPip();
    }

    @Override
    public void onStart() {
        super.onStart();
        started = true;
    }

    @Override
    public void onStop() {
        started = false;
        super.onStop();
    }

    public void setBackPressListener(BackPressListener backPressListener) {
        this.backPressListener = backPressListener;
    }

    public void setCloseListener(OnCloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public void expand() {
        if (binding == null) {
            return;
        }
        collapsed = false;
        binding.backButton.setImageResource(R.drawable.ic_baseline_arrow_back_24);
        binding.youtubePlayerView.setEnabled(true);
    }

    public void togglePip(boolean pip) {
        if (binding == null) {
            return;
        }
        binding.backButton.setVisibility(pip ? View.GONE : View.VISIBLE);
    }

    private void collapse() {
        collapsed = true;
        binding.backButton.setImageResource(R.drawable.ic_close_black_24dp);
        binding.youtubePlayerView.setEnabled(false);
    }

    public interface BackPressListener {
        void onBack();
    }

    public interface OnCloseListener {
        void closePlayer();
    }
}
