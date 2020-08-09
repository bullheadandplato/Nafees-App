package com.bullhead.nafees.android.ui.home;

import android.os.Bundle;

import com.bullhead.nafees.android.base.BaseActivity;
import com.bullhead.nafees.android.databinding.ActivityMainBinding;
import com.bullhead.nafees.android.ui.video.YoutubePlayerFragment;

public class HomeActivity extends BaseActivity {
    private ActivityMainBinding   binding;
    private TabsManager           tabsManager;
    private YoutubePlayerFragment youtubePlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tabsManager = new TabsManager(binding.bottomTabLayout, binding.viewPager, this);
        tabsManager.setup();
    }

    @Override
    protected void onUserLeaveHint() {
        goPip();
    }


    private void goPip() {
        if (shouldEnterPip()) {
            enterPictureInPictureMode();
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
}
