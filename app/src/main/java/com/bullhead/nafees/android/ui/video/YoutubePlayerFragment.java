package com.bullhead.nafees.android.ui.video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.base.BaseFragment;
import com.bullhead.nafees.android.databinding.ActivityYoutubePlayerBinding;
import com.bullhead.nafees.android.util.SimpleYoutubePlayerListener;
import com.bullhead.nafees.android.util.VideoPositionStore;
import com.bullhead.nafees.api.domain.Video;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class YoutubePlayerFragment extends BaseFragment {
    @Inject
    VideoPositionStore positionStore;
    private ActivityYoutubePlayerBinding binding;
    private Video                        video;
    private SimpleYoutubePlayerListener  playerListener;
    private boolean                      started;
    private ToggleExpandListener         toggleExpandListener;
    private OnCloseListener              closeListener;
    private boolean                      collapsed;
    private YouTubePlayer                player;

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
        binding.toggleExpand.setOnClickListener(v -> onBackClick());
        binding.closeButton.setOnClickListener(v -> {
            if (closeListener != null) {
                closeListener.closePlayer();
            }
        });
        binding.youtubePlayerView.getPlayerUiController().showYouTubeButton(false);
        binding.youtubePlayerView.getPlayerUiController().showFullscreenButton(false);
        getLifecycle().addObserver(binding.youtubePlayerView);
        if (video != null) {
            play(video);
        }
    }

    private void onBackClick() {
        if (toggleExpandListener != null) {
            collapsed = !collapsed;
            toggleExpandListener.toggleExpand(!collapsed);
        }
    }

    @Override
    public void onDestroyView() {
        getLifecycle().removeObserver(binding.youtubePlayerView);
        binding = null;
        super.onDestroyView();
    }

    public void play(@NonNull Video video) {
        if (player != null) {
            player.loadVideo(video.getId(), 0);
            return;
        }
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                youTubePlayer.loadVideo(video.getId(), 0);
                playerListener = new SimpleYoutubePlayerListener();
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

    public void setToggleExpandListener(ToggleExpandListener toggleExpandListener) {
        this.toggleExpandListener = toggleExpandListener;
    }

    public void setCloseListener(OnCloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public void expand() {
        if (binding == null) {
            return;
        }
        collapsed = false;
        binding.toggleExpand.setImageResource(R.drawable.ic_baseline_fullscreen_exit_24);
    }

    public void togglePip(boolean pip) {
        if (binding == null) {
            return;
        }
        if (pip) {
            binding.closeButton.setVisibility(View.GONE);
            binding.toggleExpand.setVisibility(View.GONE);
        } else {
            binding.closeButton.setVisibility(View.VISIBLE);
            binding.toggleExpand.setVisibility(View.VISIBLE);
        }
    }

    public void collapse() {
        if (binding == null) {
            return;
        }
        collapsed = true;
        binding.toggleExpand.setImageResource(R.drawable.ic_baseline_fullscreen_24);
    }

    public boolean isExpanded() {
        return !collapsed;
    }

    public interface ToggleExpandListener {
        void toggleExpand(boolean expand);
    }

    public interface OnCloseListener {
        void closePlayer();
    }
}
