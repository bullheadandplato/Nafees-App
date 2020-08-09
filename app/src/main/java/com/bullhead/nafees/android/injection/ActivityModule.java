package com.bullhead.nafees.android.injection;


import com.bullhead.nafees.android.ui.home.HomeActivity;
import com.bullhead.nafees.android.ui.video.YoutubePlayerFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract HomeActivity mainActivity();

    @ContributesAndroidInjector
    abstract YoutubePlayerFragment youtubePlayerActivity();
}
