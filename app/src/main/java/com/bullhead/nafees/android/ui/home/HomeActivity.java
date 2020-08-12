package com.bullhead.nafees.android.ui.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.bullhead.nafees.android.R;
import com.bullhead.nafees.android.base.BaseActivity;
import com.bullhead.nafees.android.databinding.ActivityMainBinding;
import com.bullhead.nafees.android.helper.Helper;
import com.bullhead.nafees.android.ui.video.YoutubePlayerFragment;
import com.bullhead.nafees.api.domain.Video;

public class HomeActivity extends BaseActivity {
    private ActivityMainBinding   binding;
    private TabsManager           tabsManager;
    private YoutubePlayerFragment youtubePlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tabsManager = new TabsManager(binding.bottomTabLayout,
                binding.viewPager, this);
        tabsManager.setup();
    }

    @Override
    protected void onUserLeaveHint() {
        goPip();
    }

    public void play(@NonNull Video video) {
        if (youtubePlayerFragment == null) {
            youtubePlayerFragment = new YoutubePlayerFragment();
            youtubePlayerFragment.setVideo(video);
            youtubePlayerFragment.setBackPressListener(this::onPlayerBack);
            youtubePlayerFragment.setCloseListener(this::closePlayer);
            binding.playerView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.playerView, youtubePlayerFragment)
                    .commit();
        } else {
            youtubePlayerFragment.setVideo(video);
        }
    }

    private void onPlayerBack() {
        float width  = Helper.pxFromDp(200);
        float height = Helper.pxFromDp(130);
        FrameLayout.LayoutParams params = new FrameLayout
                .LayoutParams((int) width, (int) height);
        int margin = (int) Helper.pxFromDp(16);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.setMargins(margin, margin, margin, margin);
        params.bottomMargin = binding.bottomTabLayout.getMeasuredHeight() + margin;
        binding.playerView.setLayoutParams(params);
        binding.playerView.setOnClickListener(v -> {
            expandPlayer();
        });
    }

    private void expandPlayer() {
        binding.playerView.setOnClickListener(null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        binding.playerView.setLayoutParams(params);
        youtubePlayerFragment.expand();
    }

    private void closePlayer() {
        getSupportFragmentManager().beginTransaction()
                .remove(youtubePlayerFragment)
                .commit();
        binding.playerView.setVisibility(View.GONE);
        youtubePlayerFragment = null;
    }

    private void goPip() {
        if (shouldEnterPip()) {
            enterPictureInPictureMode();
            expandPlayer();
        } else {
            super.onUserLeaveHint();
        }
    }

    @Override
    public void onBackPressed() {
        if (shouldEnterPip()) {
            goPip();
        } else {
            super.onBackPressed();
        }
    }

    private boolean shouldEnterPip() {
        return youtubePlayerFragment != null && youtubePlayerFragment.shouldEnterPip();
    }

    @Override
    protected void onDestroy() {
        tabsManager.destroy();
        super.onDestroy();
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode,
                                              Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (youtubePlayerFragment != null) {
            youtubePlayerFragment.togglePip(isInPictureInPictureMode);
        }
    }
}
