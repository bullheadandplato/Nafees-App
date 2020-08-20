package com.bullhead.nafees.android.injection;

import com.bullhead.nafees.android.ui.preferences.SettingsFragment;
import com.bullhead.nafees.android.ui.video.VideoFragment;
import com.bullhead.nafees.android.ui.video.YoutubePlayerFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract VideoFragment videoFragment();

    @ContributesAndroidInjector
    abstract YoutubePlayerFragment youtubePlayerFragment();

    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();
}
