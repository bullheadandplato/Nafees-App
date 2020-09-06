package com.bullhead.nafees.android.ui.home;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
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
import com.bullhead.nafees.android.util.bean.GoPipEvent;
import com.bullhead.nafees.api.domain.Video;

import org.greenrobot.eventbus.EventBus;

public class HomeActivity extends BaseActivity {
    private ActivityMainBinding   binding;
    private TabsManager           tabsManager;
    private YoutubePlayerFragment youtubePlayerFragment;
    private Video                 currentlyPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tabsManager = new TabsManager(binding.bottomTabLayout,
                binding.viewPager, this);
        tabsManager.setup();
        if (savedInstanceState != null) {
            currentlyPlaying = (Video) savedInstanceState.getSerializable("video");
            if (currentlyPlaying != null) {
                play(currentlyPlaying);
            }
        }
    }

    @Override
    protected void onUserLeaveHint() {
        goPip();
    }

    public void play(@NonNull Video video) {
        currentlyPlaying = video;
        if (youtubePlayerFragment == null) {
            youtubePlayerFragment = new YoutubePlayerFragment();
            youtubePlayerFragment.setVideo(video);
            youtubePlayerFragment.setToggleExpandListener(this::toggleExpand);
            youtubePlayerFragment.setCloseListener(this::closePlayer);
            binding.playerView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.playerView, youtubePlayerFragment)
                    .commit();
            expandPlayer();
        } else {
            youtubePlayerFragment.setVideo(video);
        }
    }

    private void toggleExpand(boolean expand) {
        if (expand) {
            expandPlayer();
        } else {
            collapsePlayer();
        }
    }

    private void collapsePlayer() {
        float width  = Helper.pxFromDp(200);
        float height = Helper.pxFromDp(130);
        FrameLayout.LayoutParams params = new FrameLayout
                .LayoutParams((int) width, (int) height);
        int margin = (int) Helper.pxFromDp(16);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.setMargins(margin, margin, margin, margin);
        params.bottomMargin = binding.bottomTabLayout.getMeasuredHeight() + margin;
        binding.playerView.setLayoutParams(params);
        youtubePlayerFragment.collapse();
        exitExpandedPlayUI();
    }

    private void expandPlayer() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        binding.playerView.setLayoutParams(params);
        youtubePlayerFragment.expand();
        applyExpandedPlayUI();
    }

    private void applyExpandedPlayUI() {
        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(0);
    }

    private void exitExpandedPlayUI() {
        getWindow().setStatusBarColor(style.getSecondaryColor());
        getWindow().setNavigationBarColor(style.getSecondaryColor());
        if (!isNightMode()) {
            applyLightNavigation();
        }
    }

    private void closePlayer() {
        currentlyPlaying = null;
        getSupportFragmentManager().beginTransaction()
                .remove(youtubePlayerFragment)
                .commit();
        binding.playerView.setVisibility(View.GONE);
        youtubePlayerFragment = null;
        exitExpandedPlayUI();
    }

    private void goPip() {
        if (shouldEnterPip()) {
            enterPictureInPictureMode();
            expandPlayer();
            EventBus.getDefault().post(new GoPipEvent());
        } else {
            super.onUserLeaveHint();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (currentlyPlaying != null) {
            outState.putSerializable("video", currentlyPlaying);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (youtubePlayerFragment != null && youtubePlayerFragment.isExpanded()) {
            collapsePlayer();
            return;
        }
        if (shouldEnterPip()) {
            goPip();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean isFullscreen() {
        return youtubePlayerFragment != null && youtubePlayerFragment.isExpanded();
    }

    private boolean shouldEnterPip() {
        return youtubePlayerFragment != null && youtubePlayerFragment.shouldEnterPip()
                && doesDeviceSupportPip();
    }

    private boolean doesDeviceSupportPip() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
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
